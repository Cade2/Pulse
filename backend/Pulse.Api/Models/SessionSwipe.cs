using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Pulse.Api.Models
{
    public class SessionSwipe
    {
        [Key]
        public Guid SwipeId { get; set; }

        [Required]
        public Guid SessionId { get; set; }

        [ForeignKey(nameof(SessionId))]
        public DailySession DailySession { get; set; } = null!;

        [Required]
        public Guid CardId { get; set; }

        [ForeignKey(nameof(CardId))]
        public EmotionCard EmotionCard { get; set; } = null!;

        [Required]
        public bool Accepted { get; set; }

        [Required]
        public DateTime SwipedAt { get; set; } = DateTime.UtcNow;
    }
}