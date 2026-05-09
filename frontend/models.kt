package com.example.cookingbook

import kotlinx.serialization.Serializable

/**
 * Представляет рецепт блюда в кулинарной книге.
 *
 * @property id Уникальный идентификатор рецепта (присваивается сервером)
 * @property title Название рецепта
 * @property description Подробное описание рецепта
 * @property imageUrl URL изображения блюда (опционально)
 */
@Serializable
data class Recipe(
    val id: Long = 0,
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

/**
 * Представляет один шаг приготовления рецепта.
 *
 * Шаги хранятся отдельно и связаны с рецептом через `recipeId`.
 *
 * @property id Уникальный идентификатор шага (присваивается сервером)
 * @property recipeId ID рецепта, к которому относится этот шаг
 * @property stepNumber Порядковый номер шага (1, 2, 3...)
 * @property description Описание того, что нужно сделать на этом шаге
 */
@Serializable
data class Step(
    val id: Long = 0,
    val recipeId: Long,
    val stepNumber: Int,
    val description: String
)

/**
 * DTO для ингредиента, возвращаемого с сервера.
 *
 * Используется при получении полного рецепта со всеми ингредиентами.
 *
 * @property id Уникальный идентификатор ингредиента
 * @property recipeId ID рецепта, к которому относится ингредиент
 * @property name Название ингредиента
 * @property quantity Количество ингредиента
 * @property unit Единица измерения (г, мл, шт, ч.л. и т.д.)
 */
@Serializable
data class IngredientDto(
    val id: Long = 0,
    val recipeId: Long,
    val name: String,
    val quantity: Double,
    val unit: String
)

/**
 * Запрос на создание нового рецепта (отправляется на сервер).
 *
 * Не содержит `id`, так как он генерируется на backend.
 *
 * @property title Название рецепта
 * @property description Описание рецепта
 * @property imageUrl URL изображения (опционально)
 */
@Serializable
data class RecipeRequest(
    val title: String,
    val description: String,
    val imageUrl: String? = null
)

/**
 * Запрос на добавление ингредиента к рецепту.
 *
 * @property name Название ингредиента
 * @property quantity Количество
 * @property unit Единица измерения
 */
@Serializable
data class IngredientRequest(
    val name: String,
    val quantity: Double,
    val unit: String
)

/**
 * Запрос на добавление шага приготовления к рецепту.
 *
 * @property stepNumber Порядковый номер шага
 * @property description Описание действия на этом шаге
 */
@Serializable
data class StepRequest(
    val stepNumber: Int,
    val description: String
)