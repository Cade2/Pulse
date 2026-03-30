using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

#pragma warning disable CA1814 // Prefer jagged arrays over multidimensional

namespace Pulse.Api.Migrations
{
    /// <inheritdoc />
    public partial class SeedEmotionCards : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.InsertData(
                table: "EmotionCards",
                columns: new[] { "CardId", "Category", "Description", "Emoji", "Name" },
                values: new object[,]
                {
                    { new Guid("11111111-1111-1111-1111-111111111101"), "HighPositive", "Feeling intensely enthusiastic and eager.", "⚡", "Excited" },
                    { new Guid("11111111-1111-1111-1111-111111111102"), "HighPositive", "Feeling mentally lifted and creatively energised.", "✨", "Inspired" },
                    { new Guid("11111111-1111-1111-1111-111111111103"), "HighPositive", "Feeling pleased with yourself or something you achieved.", "🏆", "Proud" },
                    { new Guid("11111111-1111-1111-1111-111111111104"), "HighPositive", "Feeling thankful and appreciative.", "🙏", "Grateful" },
                    { new Guid("11111111-1111-1111-1111-111111111105"), "HighPositive", "Feeling full of energy and ready to act.", "🔥", "Energised" },
                    { new Guid("11111111-1111-1111-1111-111111111106"), "HighPositive", "Feeling deeply happy and uplifted.", "😄", "Joyful" },
                    { new Guid("11111111-1111-1111-1111-111111111107"), "HighPositive", "Feeling sure of yourself and your ability.", "💪", "Confident" },
                    { new Guid("11111111-1111-1111-1111-111111111108"), "HighPositive", "Feeling light-hearted and fun-loving.", "🎉", "Playful" },
                    { new Guid("11111111-1111-1111-1111-111111111109"), "HighPositive", "Feeling positive about what lies ahead.", "🌱", "Hopeful" },
                    { new Guid("11111111-1111-1111-1111-111111111110"), "HighPositive", "Feeling driven to take action.", "🎯", "Motivated" },
                    { new Guid("22222222-2222-2222-2222-222222222201"), "LowPositive", "Feeling peaceful and steady.", "🌊", "Calm" },
                    { new Guid("22222222-2222-2222-2222-222222222202"), "LowPositive", "Feeling quietly satisfied with the moment.", "☀️", "Content" },
                    { new Guid("22222222-2222-2222-2222-222222222203"), "LowPositive", "Feeling free from stress or conflict.", "🕊️", "Peaceful" },
                    { new Guid("22222222-2222-2222-2222-222222222204"), "LowPositive", "Feeling secure and protected.", "🏡", "Safe" },
                    { new Guid("22222222-2222-2222-2222-222222222205"), "LowPositive", "Feeling welcomed and valued as you are.", "🤝", "Accepted" },
                    { new Guid("22222222-2222-2222-2222-222222222206"), "LowPositive", "Feeling warm, comfortable, and settled.", "🍵", "Cosy" },
                    { new Guid("22222222-2222-2222-2222-222222222207"), "LowPositive", "Feeling warmly reflective about the past.", "🌅", "Nostalgic" },
                    { new Guid("22222222-2222-2222-2222-222222222208"), "LowPositive", "Feeling soft-hearted and emotionally open.", "🌸", "Tender" },
                    { new Guid("22222222-2222-2222-2222-222222222209"), "LowPositive", "Feeling stable, centred, and present.", "🌍", "Grounded" },
                    { new Guid("22222222-2222-2222-2222-222222222210"), "LowPositive", "Feeling complete and deeply satisfied.", "✅", "Fulfilled" },
                    { new Guid("33333333-3333-3333-3333-333333333301"), "HighNegative", "Feeling worried, tense, or uneasy.", "😰", "Anxious" },
                    { new Guid("33333333-3333-3333-3333-333333333302"), "HighNegative", "Feeling overloaded and emotionally stretched.", "🌀", "Overwhelmed" },
                    { new Guid("33333333-3333-3333-3333-333333333303"), "HighNegative", "Feeling strong frustration or irritation.", "😤", "Angry" },
                    { new Guid("33333333-3333-3333-3333-333333333304"), "HighNegative", "Feeling unable to relax or settle.", "😬", "Restless" },
                    { new Guid("33333333-3333-3333-3333-333333333305"), "HighNegative", "Feeling mentally pressured and strained.", "💢", "Stressed" },
                    { new Guid("33333333-3333-3333-3333-333333333306"), "HighNegative", "Feeling envious or threatened by comparison.", "👁️", "Jealous" },
                    { new Guid("33333333-3333-3333-3333-333333333307"), "HighNegative", "Feeling a sudden rush of fear or alarm.", "😱", "Panicked" },
                    { new Guid("33333333-3333-3333-3333-333333333308"), "HighNegative", "Feeling annoyed and easily provoked.", "😠", "Irritated" },
                    { new Guid("33333333-3333-3333-3333-333333333309"), "HighNegative", "Feeling chaotic, rushed, and out of control.", "🏃", "Frantic" },
                    { new Guid("33333333-3333-3333-3333-333333333310"), "HighNegative", "Feeling guarded or ready to protect yourself.", "🛡️", "Defensive" },
                    { new Guid("44444444-4444-4444-4444-444444444401"), "LowNegative", "Feeling down or emotionally heavy.", "😔", "Sad" },
                    { new Guid("44444444-4444-4444-4444-444444444402"), "LowNegative", "Feeling hollow or emotionally drained.", "🕳️", "Empty" },
                    { new Guid("44444444-4444-4444-4444-444444444403"), "LowNegative", "Feeling disconnected from emotion.", "🌫️", "Numb" },
                    { new Guid("44444444-4444-4444-4444-444444444404"), "LowNegative", "Feeling isolated or emotionally alone.", "🚶", "Lonely" },
                    { new Guid("44444444-4444-4444-4444-444444444405"), "LowNegative", "Feeling like things will not improve.", "🌧️", "Hopeless" },
                    { new Guid("44444444-4444-4444-4444-444444444406"), "LowNegative", "Feeling exhausted and depleted.", "🪫", "Drained" },
                    { new Guid("44444444-4444-4444-4444-444444444407"), "LowNegative", "Feeling detached from people or the moment.", "📵", "Disconnected" },
                    { new Guid("44444444-4444-4444-4444-444444444408"), "LowNegative", "Feeling embarrassed or deeply self-critical.", "😞", "Ashamed" },
                    { new Guid("44444444-4444-4444-4444-444444444409"), "LowNegative", "Feeling unstimulated or uninterested.", "😑", "Bored" },
                    { new Guid("44444444-4444-4444-4444-444444444410"), "LowNegative", "Feeling unseen or overlooked.", "👻", "Invisible" }
                });
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111101"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111102"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111103"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111104"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111105"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111106"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111107"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111108"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111109"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("11111111-1111-1111-1111-111111111110"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222201"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222202"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222203"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222204"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222205"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222206"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222207"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222208"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222209"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("22222222-2222-2222-2222-222222222210"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333301"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333302"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333303"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333304"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333305"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333306"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333307"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333308"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333309"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("33333333-3333-3333-3333-333333333310"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444401"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444402"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444403"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444404"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444405"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444406"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444407"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444408"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444409"));

            migrationBuilder.DeleteData(
                table: "EmotionCards",
                keyColumn: "CardId",
                keyValue: new Guid("44444444-4444-4444-4444-444444444410"));
        }
    }
}
