using System.ComponentModel.DataAnnotations;

namespace Pulse.Api.Models
{
    public class User
    {
        [Key]
        public Guid UserId { get; set; }

        [Required]
        [EmailAddress]
        [MaxLength(255)]
        public string Email { get; set; } = string.Empty;

        [Required]
        public string PasswordHash { get; set; } = string.Empty;

        [Required]
        [MaxLength(100)]
        public string DisplayName { get; set; } = string.Empty;

        [MaxLength(7)]
        public string AvatarColour { get; set; } = "#A78BFA";

        public TimeOnly NotificationTime { get; set; } = new TimeOnly(20, 0);

        public string? FCMToken { get; set; }

        public string? APNSToken { get; set; }

        public string? RefreshToken { get; set; }

        public DateTime? RefreshTokenExpiresAt { get; set; }

        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;

        public ICollection<DailySession> DailySessions { get; set; } = new List<DailySession>();
    }
}