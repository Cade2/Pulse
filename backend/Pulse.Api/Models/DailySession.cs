using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pulse.Api.Models
{
    public class DailySession
    {
        [Key]
        public Guid SessionId { get; set; }

        [Required]
        public Guid UserId { get; set; }

        [ForeignKey(nameof(UserId))]
        public User User { get; set; } = null!;

        [Required]
        public DateOnly Date { get; set; }

        public DateTime? CompletedAt { get; set; }

        [MaxLength(50)]
        public string? ContextSocial { get; set; }

        [MaxLength(50)]
        public string? ContextEnergy { get; set; }

        [MaxLength(50)]
        public string? ContextSleep { get; set; }

        public ICollection<SessionSwipe> SessionSwipes { get; set; } = new List<SessionSwipe>();
    }
}