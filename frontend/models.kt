package com.example.cookingbook

import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long = 0,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

@Serializable
data class Step(
    val id: Long = 0,
    val recipeId: Long,
    val stepNumber: Int,
    val description: String
)

@Serializable
data class IngredientDto(
    val id: Long = 0,
    val recipeId: Long,
    val name: String,
    val quantity: Double,
    val unit: String
)

@Serializable
data class RecipeRequest(
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

@Serializable
data class IngredientRequest(
    val name: String,
    val quantity: Double,
    val unit: String
)

@Serializable
data class StepRequest(
    val stepNumber: Int,
    val description: String
)