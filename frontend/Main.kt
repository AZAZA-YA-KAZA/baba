import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
    val recipes = remember { mutableStateOf<List<Recipe>>(emptyList()) }
    val error = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            recipes.value = ApiClient.getRecipes()
        } catch (e: Exception) {
            error.value = "Error: ${e.message}"
        }
    }

    Div {
        if (error.value != null) {
            Text(error.value!!)
        } else if (recipes.value.isEmpty()) {
            Text("Loading recipes...")
        } else {
            recipes.value.forEach { recipe ->
                Div { Text(recipe.title) }
            }
        }
    }

    Button(attrs = { onClick { /* Добавьте логику */ } }) {
        Text("Add Recipe")
    }
}
}
