package com.muthu.fourtitude.ui.details

import com.muthu.fourtitude.storage.RecipeDatabase
import com.muthu.fourtitude.storage.RecipeEntity


class DetailsActivityBrain {

    fun removeItemFromDatabase(
        recipeDatabase: RecipeDatabase,
        itemToBeRemoved: RecipeEntity
    ) {
        recipeDatabase.recipeDao().deleteSelectedRecipe(itemToBeRemoved)

    }
}