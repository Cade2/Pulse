using System.ComponentModel.DataAnnotations;

namespace Pulse.Api.DTOs.Auth
{
    public class FcmTokenRequestDto
    {
        [Required]
        public string FcmToken { get; set; } = string.Empty;
    }
}