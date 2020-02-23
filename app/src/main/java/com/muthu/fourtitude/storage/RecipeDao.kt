package com.muthu.fourtitude.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface RecipeDao {

    //insert
    @Insert(onConflict = REPLACE)
    fun insertRecipe(vararg recipeEntity: RecipeEntity)

    //select all
    @Query("SELECT * FROM recipe ORDER BY id DESC")
    fun getAllReceipeList(): LiveData<List<RecipeEntity?>?>

    //Delete selected
    @Delete
    fun deleteSelectedRecipe(vararg recipeEntity: RecipeEntity)

    //updated
    @Query("UPDATE recipe SET category = :category,  image = :image, categoryId = :categoryId, ingredient = :ingredients, duration = :duration WHERE id = :id")
    fun updateSelectedRecipeByID(
        id: Int,
        category: String?,
        image: String?,
        ingredients: String?,
        duration: String,
        categoryId: String
    )


}