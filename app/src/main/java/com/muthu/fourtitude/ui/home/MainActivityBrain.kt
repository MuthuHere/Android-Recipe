package com.muthu.fourtitude.ui.home

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.muthu.fourtitude.data.RecipeData
import com.muthu.fourtitude.storage.RecipeDatabase
import com.muthu.fourtitude.storage.RecipeEntity
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import java.io.IOException
import kotlin.properties.Delegates


class MainActivityBrain {

    var recipeData: RecipeData by Delegates.notNull()

    fun syncRecipe(recipeDatabase: RecipeDatabase, context: Context) {
        recipeDatabase.recipeDao().getAllReceipeList().observeForever {
            if (!it?.isNotEmpty()!!) {
                getXMLFromAssets(context, recipeDatabase)
            }
        }

    }


    private fun getXMLFromAssets(
        context: Context,
        recipeDatabase: RecipeDatabase
    ) {
        val assetManager: AssetManager = context.assets

        try {
            //getting xml file from assets
            val inputStream = assetManager.open("recipetypes.xml")

            //getting file Size


            val length: Int = inputStream.available()
            val data = ByteArray(length)
            inputStream.read(data)
            val strXML = String(data)


            //converting xml to json for simplifying
            val xmlToJson = XmlToJson.Builder(strXML).build()
            //converting into respected model

            recipeData = Gson().fromJson(xmlToJson.toString(), RecipeData::class.java)

            //insert into databse
            insertIntoDatabase(recipeDatabase)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun insertIntoDatabase(recipeDatabase: RecipeDatabase) {

        recipeData.RecipeTypes?.RecipeData?.Recipe?.forEachIndexed { _, recipes ->
            recipeDatabase.recipeDao().insertRecipe(
                RecipeEntity(
                    categoryName = recipes?.Name!!,
                    image = recipes.Image!!,
                    duration = recipes.Duration!!,
                    ingredient = recipes.Ingredients!!,
                    categoryID = recipes.CatId!!,
                    steps = recipes.Steps!!
                )
            )

        }


    }


}

