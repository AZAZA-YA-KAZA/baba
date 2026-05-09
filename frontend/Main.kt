package com.example.cookingbook

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

/**
 * Точка входа в веб-приложение "Кулинарная книга".
 *
 * Функция монтирует Compose for Web приложение в DOM-элемент с id `"root"`.
 *
 * ### Основная функциональность:
 * - При запуске автоматически загружает список всех рецептов через `ApiClient.getRecipes()`
 * - Отображает состояние загрузки, список рецептов или сообщение об ошибке
 * - Содержит кнопку для добавления нового рецепта (логика пока заглушка)
 *
 * Использует:
 * - `Compose for Web` (JetBrains Compose HTML)
 * - `mutableStateOf` + `remember` для управления состоянием
 * - `LaunchedEffect` для выполнения side-effect'ов (загрузка данных при монтировании)
 *
 * @see ApiClient
 * @see Recipe
 */
fun main() {
    renderComposable(rootElementId = "root") {

        // Состояние списка рецептов
        val recipes = remember { mutableStateOf<List<Recipe>>(emptyList()) }

        // Состояние ошибки при загрузке данных
        val error = remember { mutableStateOf<String?>(null) }

        // Загрузка рецептов при первом рендере компонента
        LaunchedEffect(Unit) {
            try {
                recipes.value = ApiClient.getRecipes()
            } catch (e: Exception) {
                error.value = "Error: ${e.message}"
            }
        }

        Div {
            when {
                error.value != null -> {
                    Text(error.value!!)
                }
                recipes.value.isEmpty() -> {
                    Text("Loading recipes...")
                }
                else -> {
                    recipes.value.forEach { recipe ->
                        Div { Text(recipe.title) }
                    }
                }
            }
        }

        // Кнопка добавления нового рецепта
        Button(attrs = {
            onClick {
                // TODO: Реализовать открытие формы добавления рецепта
            }
        }) {
            Text("Add Recipe")
        }
    }
}