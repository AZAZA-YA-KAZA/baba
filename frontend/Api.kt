package com.example.cookingbook

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Singleton-объект, предоставляющий HTTP-клиент для взаимодействия с backend API кулинарной книги.
 * Base URL: `http://localhost:8080/api/kulinare`
 *
 * ### Основные возможности:
 * - Получение списка рецептов и отдельных рецептов
 * - Добавление новых рецептов и шагов приготовления
 * - Удаление рецептов
 *
 * @see Recipe
 * @see RecipeRequest
 * @see Step
 * @see StepRequest
 */
object ApiClient {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private const val BASE_URL = "http://localhost:8080/api/kulinare"

    /**
     * Получает список всех рецептов из backend.
     *
     * @return Список всех доступных рецептов
     * @throws Exception в случае сетевой ошибки или ошибки десериализации
     */
    suspend fun getRecipes(): List<Recipe> {
        return client.get("$BASE_URL/recipes").body()
    }

    /**
     * Получает рецепт по его идентификатору.
     *
     * @param id уникальный идентификатор рецепта
     * @return рецепт с указанным ID или `null`, если рецепт не найден
     */
    suspend fun getRecipe(id: Long): Recipe? {
        return client.get("$BASE_URL/recipe/$id").body()
    }

    /**
     * Добавляет новый рецепт в систему.
     *
     * @param recipeRequest данные нового рецепта
     * @return созданный рецепт (с присвоенным ID и другими полями от сервера)
     */
    suspend fun addRecipe(recipeRequest: RecipeRequest): Recipe {
        return client.post("$BASE_URL/recipe") {
            contentType(ContentType.Application.Json)
            setBody(recipeRequest)
        }.body()
    }

    /**
     * Добавляет новый шаг приготовления к существующему рецепту.
     *
     * @param idRecipe идентификатор рецепта, к которому добавляется шаг
     * @param stepRequest данные шага приготовления
     * @return созданный шаг
     */
    suspend fun addStep(idRecipe: Long, stepRequest: StepRequest): Step {
        return client.post("$BASE_URL/$idRecipe/step") {
            contentType(ContentType.Application.Json)
            setBody(stepRequest)
        }.body()
    }

    /**
     * Получает список всех шагов приготовления для указанного рецепта.
     *
     * @param idRecipe идентификатор рецепта
     * @return список шагов в порядке их выполнения
     */
    suspend fun getSteps(idRecipe: Long): List<Step> {
        return client.get("$BASE_URL/$idRecipe/step").body()
    }

    /**
     * Удаляет рецепт по идентификатору.
     *
     * @param id идентификатор удаляемого рецепта
     * @return сообщение от сервера (обычно "Recipe deleted" или подобное)
     */
    suspend fun deleteRecipe(id: Long): String {
        return client.delete("$BASE_URL/recipe/$id").body()
    }

    /**
     * Закрывает HTTP-клиент и освобождает все ресурсы (соединения, потоки).
     *
     * Вызывайте этот метод при завершении работы приложения или когда клиент больше не нужен.
     */
    fun close() {
        client.close()
    }
}