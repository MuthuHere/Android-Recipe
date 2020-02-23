package com.muthu.fourtitude.data

data class RecipeData(
    val RecipeTypes: RecipeTypes?
)


data class RecipeTypes(
    val CatogryData: CatogryData?,
    val RecipeData: RecipeDataX?
)

data class CatogryData(
    val Category: ArrayList<Category?>?
)

data class RecipeDataX(
    val Recipe: List<Recipe?>?
)

data class Recipe(
    val CatId: String?,
    val Duration: String?,
    val Image: String?,
    val Ingredients: String?,
    val Steps: String?,
    val Name: String?
)

data class Category(
    val CatName: String?,
    val Id: String?
)