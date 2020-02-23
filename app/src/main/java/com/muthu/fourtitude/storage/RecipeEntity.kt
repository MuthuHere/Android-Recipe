package com.muthu.fourtitude.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "category")
    var categoryName: String,
    var image: String,
    var duration: String,
    var ingredient: String,
    var steps: String,
    var categoryID: String


)