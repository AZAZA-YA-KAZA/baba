// Автотесты API для проекта "Кулинарная книга".
// Запуск: node api-tests-cooking-book.js
// Перед запуском backend должен работать на http://localhost:8080

const API = 'http://127.0.0.1:8080/api/kulinare';
async function request(name, path, options = {}) {
    const url = `${API}${path}`;
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    const text = await response.text();
    let body = null;

    try {
        body = text ? JSON.parse(text) : null;
    } catch (e) {
        body = text;
    }

    if (!response.ok) {
        throw new Error(`${name}: HTTP ${response.status}. Ответ сервера: ${text}`);
    }

    console.log(`OK: ${name}`);
    return body;
}

function assert(condition, message) {
    if (!condition) {
        throw new Error(`Проверка не пройдена: ${message}`);
    }
}

async function runTests() {
    console.log("Начало тестирования API проекта 'Кулинарная книга'\n");

    // 1. Получение списка рецептов
    const recipesBefore = await request("Получение списка рецептов", "/recipes");
    assert(Array.isArray(recipesBefore), "GET /recipes должен вернуть массив");

    // 2. Создание рецепта
    const createdRecipe = await request("Создание рецепта", "/recipe", {
        method: "POST",
        body: JSON.stringify({
            title: "Тестовый рецепт",
            description: "Рецепт создан автоматическим тестом",
            photoUrl: "https://example.com/test.jpg"
        })
    });
    assert(createdRecipe.idRecipe, "После создания рецепта должен вернуться idRecipe");
    const recipeId = createdRecipe.idRecipe;

    // 3. Получение созданного рецепта
    const loadedRecipe = await request("Получение рецепта по ID", `/recipe/${recipeId}`);
    assert(loadedRecipe.title === "Тестовый рецепт", "Название рецепта должно совпадать");

    // 4. Добавление ингредиента
    const createdIngredient = await request("Добавление ингредиента", `/${recipeId}/ingredient`, {
        method: "POST",
        body: JSON.stringify({
            name: "Мука",
            count: 300,
            unit: "г"
        })
    });
    assert(createdIngredient.idIngredient, "После добавления ингредиента должен вернуться idIngredient");
    const ingredientId = createdIngredient.idIngredient;

    // 5. Получение ингредиентов рецепта
    const ingredients = await request("Получение ингредиентов рецепта", `/${recipeId}/ingredient`);
    assert(Array.isArray(ingredients), "Список ингредиентов должен быть массивом");
    assert(ingredients.length > 0, "В рецепте должен быть хотя бы один ингредиент");

    // 6. Добавление шага приготовления
    const createdStep = await request("Добавление шага приготовления", `/${recipeId}/step`, {
        method: "POST",
        body: JSON.stringify({
            stepDescription: "Смешать ингредиенты",
            stepOrder: 1
        })
    });
    assert(createdStep.idStep, "После добавления шага должен вернуться idStep");
    const stepId = createdStep.idStep;

    // 7. Получение шагов рецепта
    const steps = await request("Получение шагов рецепта", `/${recipeId}/step`);
    assert(Array.isArray(steps), "Список шагов должен быть массивом");
    assert(steps.length > 0, "В рецепте должен быть хотя бы один шаг");

    // 8. Редактирование рецепта
    const updatedRecipe = await request("Редактирование рецепта", `/recipe/${recipeId}`, {
        method: "PUT",
        body: JSON.stringify({
            title: "Обновленный тестовый рецепт",
            description: "Описание изменено автоматическим тестом",
            photoUrl: "https://example.com/updated.jpg"
        })
    });
    assert(updatedRecipe.title === "Обновленный тестовый рецепт", "Название должно измениться");

    // 9. Редактирование ингредиента в рецепте
    const updatedIngredientLink = await request("Редактирование ингредиента рецепта", `/ingredient/${recipeId}/${ingredientId}`, {
        method: "PUT",
        body: JSON.stringify({
            name: "Мука",
            count: 500,
            unit: "г"
        })
    });
    assert(updatedIngredientLink.count === 500, "Количество ингредиента должно измениться на 500");

    // 10. Редактирование шага
    const updatedStep = await request("Редактирование шага", `/step/${stepId}`, {
        method: "PUT",
        body: JSON.stringify({
            stepDescription: "Смешать ингредиенты до однородной массы",
            stepOrder: 1
        })
    });
    assert(updatedStep.stepDescription.includes("однородной"), "Описание шага должно измениться");

    // 11. Поиск рецепта в интернете. Тест может быть пропущен, если нет доступа к внешнему API.
    try {
        const internetRecipes = await request("Поиск рецепта в интернете", "/internet/search?query=cake");
        assert(Array.isArray(internetRecipes), "Поиск в интернете должен вернуть массив");
    } catch (error) {
        console.log(`SKIP: Поиск в интернете не проверен: ${error.message}`);
    }

    // 12. Удаление тестовых данных
    await request("Удаление шага", `/step/${recipeId}/${stepId}`, { method: "DELETE" });
    await request("Удаление ингредиента из рецепта", `/${recipeId}/${ingredientId}`, { method: "DELETE" });
    await request("Удаление рецепта", `/recipe/${recipeId}`, { method: "DELETE" });

    console.log("\nВсе основные API-тесты выполнены успешно.");
}

runTests().catch(error => {
    console.error("\nТестирование завершилось ошибкой:");
    console.error(error.message);
    process.exit(1);
});
