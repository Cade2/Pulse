using System.ComponentModel.DataAnnotations;

namespace Pulse.Api.DTOs.Auth
{
    public class RegisterRequestDto
    {
        [Required]
        [EmailAddress]
        [MaxLength(255)]
        public string Email { get; set; } = string.Empty;

        [Required]
        [MinLength(6)]
        [MaxLength(100)]
        public string Password { get; set; } = string.Empty;

        [Required]
        [MaxLength(100)]
        public string DisplayName { get; set; } = string.Empty;
    }
}