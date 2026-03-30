using System.ComponentModel.DataAnnotations;

namespace Pulse.Api.DTOs.Auth
{
    public class RefreshRequestDto
    {
        [Required]
        public string RefreshToken { get; set; } = string.Empty;
    }
}