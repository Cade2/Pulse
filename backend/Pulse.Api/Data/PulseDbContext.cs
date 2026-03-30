using Microsoft.EntityFrameworkCore;
using Pulse.Api.Models;

namespace Pulse.Api.Data
{
    public class PulseDbContext : DbContext
    {
        public PulseDbContext(DbContextOptions<PulseDbContext> options) : base(options)
        {
        }

        public DbSet<User> Users => Set<User>();
        public DbSet<EmotionCard> EmotionCards => Set<EmotionCard>();
        public DbSet<DailySession> DailySessions => Set<DailySession>();
        public DbSet<SessionSwipe> SessionSwipes => Set<SessionSwipe>();

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<User>()
                .HasIndex(u => u.Email)
                .IsUnique();

            modelBuilder.Entity<DailySession>()
                .HasIndex(s => new { s.UserId, s.Date })
                .IsUnique();

            modelBuilder.Entity<DailySession>()
                .HasOne(s => s.User)
                .WithMany(u => u.DailySessions)
                .HasForeignKey(s => s.UserId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<SessionSwipe>()
                .HasOne(ss => ss.DailySession)
                .WithMany(ds => ds.SessionSwipes)
                .HasForeignKey(ss => ss.SessionId)
                .OnDelete(DeleteBehavior.Cascade);

            modelBuilder.Entity<SessionSwipe>()
                .HasOne(ss => ss.EmotionCard)
                .WithMany(ec => ec.SessionSwipes)
                .HasForeignKey(ss => ss.CardId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<EmotionCard>().HasData(EmotionCardSeed.GetEmotionCards());
        }
    }
}