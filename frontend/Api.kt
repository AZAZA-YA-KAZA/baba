package com.example.cookingbook

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

// HttpClient для взаимодействия с бэкендом
object ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private const val BASE_URL = "http://localhost:8080/api/kulinare"

    // Получение всех рецептов
    suspend fun getRecipes(): List<Recipe> {
        return client.get("$BASE_URL/recipes").body()
    }

    // Получение конкретного рецепта по ID
    suspend fun getRecipe(id: Long): Recipe? {
        return client.get("$BASE_URL/recipe/$id").body()
    }

    // Добавление нового рецепта
    suspend fun addRecipe(recipeRequest: RecipeRequest): Recipe {
        return client.post("$BASE_URL/recipe") {
            contentType(ContentType.Application.Json)
            setBody(recipeRequest)
        }.body()
    }

    // Создание шага для рецепта
    suspend fun addStep(idRecipe: Long, stepRequest: StepRequest): Step {
        return client.post("$BASE_URL/$idRecipe/step") {
            contentType(ContentType.Application.Json)
            setBody(stepRequest)
        }.body()
    }

    // Получение списка шагов для рецепта
    suspend fun getSteps(idRecipe: Long): List<Step> {
        return client.get("$BASE_URL/$idRecipe/step").body()
    }

    // Удаление рецепта
    suspend fun deleteRecipe(id: Long): String {
        return client.delete("$BASE_URL/recipe/$id").body()
    }
}
