using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Pulse.Api.Migrations
{
    /// <inheritdoc />
    public partial class InitialCreate : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "EmotionCards",
                columns: table => new
                {
                    CardId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    Name = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    Description = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    Emoji = table.Column<string>(type: "nvarchar(10)", maxLength: 10, nullable: false),
                    Category = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_EmotionCards", x => x.CardId);
                });

            migrationBuilder.CreateTable(
                name: "Users",
                columns: table => new
                {
                    UserId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    Email = table.Column<string>(type: "nvarchar(255)", maxLength: 255, nullable: false),
                    PasswordHash = table.Column<string>(type: "nvarchar(max)", nullable: false),
                    DisplayName = table.Column<string>(type: "nvarchar(100)", maxLength: 100, nullable: false),
                    AvatarColour = table.Column<string>(type: "nvarchar(7)", maxLength: 7, nullable: false),
                    NotificationTime = table.Column<TimeOnly>(type: "time", nullable: false),
                    FCMToken = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    APNSToken = table.Column<string>(type: "nvarchar(max)", nullable: true),
                    CreatedAt = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Users", x => x.UserId);
                });

            migrationBuilder.CreateTable(
                name: "DailySessions",
                columns: table => new
                {
                    SessionId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    UserId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    Date = table.Column<DateOnly>(type: "date", nullable: false),
                    CompletedAt = table.Column<DateTime>(type: "datetime2", nullable: true),
                    ContextSocial = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: true),
                    ContextEnergy = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: true),
                    ContextSleep = table.Column<string>(type: "nvarchar(50)", maxLength: 50, nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_DailySessions", x => x.SessionId);
                    table.ForeignKey(
                        name: "FK_DailySessions_Users_UserId",
                        column: x => x.UserId,
                        principalTable: "Users",
                        principalColumn: "UserId",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateTable(
                name: "SessionSwipes",
                columns: table => new
                {
                    SwipeId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    SessionId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    CardId = table.Column<Guid>(type: "uniqueidentifier", nullable: false),
                    Accepted = table.Column<bool>(type: "bit", nullable: false),
                    SwipedAt = table.Column<DateTime>(type: "datetime2", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_SessionSwipes", x => x.SwipeId);
                    table.ForeignKey(
                        name: "FK_SessionSwipes_DailySessions_SessionId",
                        column: x => x.SessionId,
                        principalTable: "DailySessions",
                        principalColumn: "SessionId",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_SessionSwipes_EmotionCards_CardId",
                        column: x => x.CardId,
                        principalTable: "EmotionCards",
                        principalColumn: "CardId",
                        onDelete: ReferentialAction.Restrict);
                });

            migrationBuilder.CreateIndex(
                name: "IX_DailySessions_UserId_Date",
                table: "DailySessions",
                columns: new[] { "UserId", "Date" },
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_SessionSwipes_CardId",
                table: "SessionSwipes",
                column: "CardId");

            migrationBuilder.CreateIndex(
                name: "IX_SessionSwipes_SessionId",
                table: "SessionSwipes",
                column: "SessionId");

            migrationBuilder.CreateIndex(
                name: "IX_Users_Email",
                table: "Users",
                column: "Email",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "SessionSwipes");

            migrationBuilder.DropTable(
                name: "DailySessions");

            migrationBuilder.DropTable(
                name: "EmotionCards");

            migrationBuilder.DropTable(
                name: "Users");
        }
    }
}
