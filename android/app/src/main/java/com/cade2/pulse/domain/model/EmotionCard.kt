package com.cade2.pulse.domain.model

data class EmotionCard(
    val cardId: String,
    val name: String,
    val description: String,
    val emoji: String,
    val category: String
)
