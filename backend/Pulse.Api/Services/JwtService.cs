using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Microsoft.IdentityModel.Tokens;
using Pulse.Api.Models;

namespace Pulse.Api.Services
{
    public class JwtService : IJwtService
    {
        private readonly IConfiguration _configuration;

        public JwtService(IConfiguration configuration)
        {
            _configuration = configuration;
        }

        public string GenerateAccessToken(User user)
        {
            var key = _configuration["Jwt:Key"]
                ?? throw new InvalidOperationException("JWT key is not configured.");

            var issuer = _configuration["Jwt:Issuer"]
                ?? throw new InvalidOperationException("JWT issuer is not configured.");

            var audience = _configuration["Jwt:Audience"]
                ?? throw new InvalidOperationException("JWT audience is not configured.");

            var expiresAt = GetAccessTokenExpiryUtc();

            var claims = new List<Claim>
            {
                new(JwtRegisteredClaimNames.Sub, user.UserId.ToString()),
                new(ClaimTypes.NameIdentifier, user.UserId.ToString()),
                new(JwtRegisteredClaimNames.Email, user.Email),
                new(ClaimTypes.Email, user.Email),
                new(ClaimTypes.Name, user.DisplayName),
                new(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
            };

            var credentials = new SigningCredentials(
                new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key)),
                SecurityAlgorithms.HmacSha256);

            var token = new JwtSecurityToken(
                issuer: issuer,
                audience: audience,
                claims: claims,
                expires: expiresAt,
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public string GenerateRefreshToken()
        {
            var randomBytes = RandomNumberGenerator.GetBytes(64);
            return Convert.ToBase64String(randomBytes);
        }

        public DateTime GetAccessTokenExpiryUtc()
        {
            var expiryMinutes = _configuration.GetValue<int>("Jwt:ExpiryMinutes");
            return DateTime.UtcNow.AddMinutes(expiryMinutes);
        }

        public DateTime GetRefreshTokenExpiryUtc()
        {
            var expiryDays = _configuration.GetValue<int>("Jwt:RefreshTokenExpiryDays");
            return DateTime.UtcNow.AddDays(expiryDays);
        }
    }
}