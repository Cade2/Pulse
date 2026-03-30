using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Pulse.Api.Data;
using Pulse.Api.DTOs.Auth;
using Pulse.Api.Models;
using Pulse.Api.Services;

namespace Pulse.Api.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly PulseDbContext _context;
        private readonly IJwtService _jwtService;

        public AuthController(PulseDbContext context, IJwtService jwtService)
        {
            _context = context;
            _jwtService = jwtService;
        }

        [HttpPost("register")]
        public async Task<ActionResult<AuthResponseDto>> Register(RegisterRequestDto request)
        {
            var normalizedEmail = request.Email.Trim().ToLowerInvariant();

            var userExists = await _context.Users.AnyAsync(u => u.Email == normalizedEmail);
            if (userExists)
            {
                return Conflict(new { message = "An account with this email already exists." });
            }

            var refreshToken = _jwtService.GenerateRefreshToken();
            var refreshTokenExpiresAt = _jwtService.GetRefreshTokenExpiryUtc();

            var user = new User
            {
                UserId = Guid.NewGuid(),
                Email = normalizedEmail,
                DisplayName = request.DisplayName.Trim(),
                PasswordHash = BCrypt.Net.BCrypt.HashPassword(request.Password),
                RefreshToken = refreshToken,
                RefreshTokenExpiresAt = refreshTokenExpiresAt,
                CreatedAt = DateTime.UtcNow
            };

            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            return Ok(BuildAuthResponse(user));
        }

        [HttpPost("login")]
        public async Task<ActionResult<AuthResponseDto>> Login(LoginRequestDto request)
        {
            var normalizedEmail = request.Email.Trim().ToLowerInvariant();

            var user = await _context.Users.SingleOrDefaultAsync(u => u.Email == normalizedEmail);
            if (user is null || !BCrypt.Net.BCrypt.Verify(request.Password, user.PasswordHash))
            {
                return Unauthorized(new { message = "Invalid email or password." });
            }

            user.RefreshToken = _jwtService.GenerateRefreshToken();
            user.RefreshTokenExpiresAt = _jwtService.GetRefreshTokenExpiryUtc();

            await _context.SaveChangesAsync();

            return Ok(BuildAuthResponse(user));
        }

        [HttpPost("refresh")]
        public async Task<ActionResult<RefreshResponseDto>> Refresh(RefreshRequestDto request)
        {
            var incomingRefreshToken = request.RefreshToken.Trim();

            var user = await _context.Users.SingleOrDefaultAsync(u => u.RefreshToken == incomingRefreshToken);
            if (user is null)
            {
                return Unauthorized(new { message = "Invalid refresh token." });
            }

            if (!user.RefreshTokenExpiresAt.HasValue || user.RefreshTokenExpiresAt.Value <= DateTime.UtcNow)
            {
                return Unauthorized(new { message = "Refresh token has expired." });
            }

            var accessTokenExpiresAt = _jwtService.GetAccessTokenExpiryUtc();
            var refreshTokenExpiresAt = _jwtService.GetRefreshTokenExpiryUtc();
            var newRefreshToken = _jwtService.GenerateRefreshToken();

            user.RefreshToken = newRefreshToken;
            user.RefreshTokenExpiresAt = refreshTokenExpiresAt;

            await _context.SaveChangesAsync();

            return Ok(new RefreshResponseDto
            {
                AccessToken = _jwtService.GenerateAccessToken(user),
                RefreshToken = newRefreshToken,
                AccessTokenExpiresAt = accessTokenExpiresAt,
                RefreshTokenExpiresAt = refreshTokenExpiresAt
            });
        }

        [Authorize]
        [HttpPost("fcm-token")]
        public async Task<IActionResult> SaveFcmToken(FcmTokenRequestDto request)
        {
            var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier)
                              ?? User.FindFirstValue("sub");

            if (!Guid.TryParse(userIdClaim, out var userId))
            {
                return Unauthorized(new { message = "Invalid user token." });
            }

            var user = await _context.Users.FindAsync(userId);
            if (user is null)
            {
                return NotFound(new { message = "User not found." });
            }

            user.FCMToken = request.FcmToken.Trim();
            await _context.SaveChangesAsync();

            return Ok(new { message = "FCM token saved successfully." });
        }

        private AuthResponseDto BuildAuthResponse(User user)
        {
            var accessTokenExpiresAt = _jwtService.GetAccessTokenExpiryUtc();
            var refreshTokenExpiresAt = user.RefreshTokenExpiresAt ?? _jwtService.GetRefreshTokenExpiryUtc();

            return new AuthResponseDto
            {
                UserId = user.UserId,
                Email = user.Email,
                DisplayName = user.DisplayName,
                AccessToken = _jwtService.GenerateAccessToken(user),
                RefreshToken = user.RefreshToken ?? string.Empty,
                AccessTokenExpiresAt = accessTokenExpiresAt,
                RefreshTokenExpiresAt = refreshTokenExpiresAt
            };
        }
    }
}