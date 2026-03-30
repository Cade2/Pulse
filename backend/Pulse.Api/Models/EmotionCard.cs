using System.ComponentModel.DataAnnotations;

namespace Pulse.Api.Models
{
    public class EmotionCard
    {
        [Key]
        public Guid CardId { get; set; }

        [Required]
        [MaxLength(100)]
        public string Name { get; set; } = string.Empty;

        [Required]
        [MaxLength(255)]
        public string Description { get; set; } = string.Empty;

        [Required]
        [MaxLength(10)]
        public string Emoji { get; set; } = string.Empty;

        [Required]
        [MaxLength(50)]
        public string Category { get; set; } = string.Empty;

        public ICollection<SessionSwipe> SessionSwipes { get; set; } = new List<SessionSwipe>();
    }
}