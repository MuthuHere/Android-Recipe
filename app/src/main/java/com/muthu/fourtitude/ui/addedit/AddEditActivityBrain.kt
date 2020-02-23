package com.muthu.fourtitude.ui.addedit

import com.muthu.fourtitude.storage.RecipeDatabase
import com.muthu.fourtitude.storage.RecipeEntity

class AddEditActivityBrain {


    fun updateRecipe(
        id: Int,
        recipeDatabase: RecipeDatabase, input: RecipeEntity
    ) {
        recipeDatabase.recipeDao().updateSelectedRecipeByID(
            id = id,
            category = input.categoryName,
            image = input.image,
            ingredients = input.ingredient,
            duration = input.duration, categoryId = input.categoryID
        )

    }

    fun createRecipe(recipeDatabase: RecipeDatabase, input: RecipeEntity) {
        recipeDatabase.recipeDao().insertRecipe(
            input
        )


    }
}