using Pulse.Api.Models;

namespace Pulse.Api.Services
{
    public interface IJwtService
    {
        string GenerateAccessToken(User user);
        string GenerateRefreshToken();
        DateTime GetAccessTokenExpiryUtc();
        DateTime GetRefreshTokenExpiryUtc();
    }
}