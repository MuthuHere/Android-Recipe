package com.muthu.fourtitude.ui.details


import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.gson.Gson
import com.muthu.fourtitude.R
import com.muthu.fourtitude.base.BaseActivity
import com.muthu.fourtitude.storage.RecipeEntity
import com.muthu.fourtitude.ui.addedit.AddEditActivity
import com.muthu.fourtitude.utils.AppDialogs
import com.muthu.fourtitude.utils.AppDialogs.showDialogWith2Buttons
import com.muthu.fourtitude.utils.AppMessages.toast
import com.muthu.fourtitude.utils.ImageHelper.appImageHelper
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.content_recipe_details.*

class RecipeDetailsActivity : BaseActivity() {

    private lateinit var dataOfRecipe: RecipeEntity
    private val detailsActivityBrain = DetailsActivityBrain()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIntentData()

        bindData()

        clickEvents()

    }

    private fun clickEvents() {
        btnDelete.setOnClickListener(this)
        btnEdit.setOnClickListener(this)
    }

    private fun bindData() {

        if (dataOfRecipe == null) {
            return
        }
        dataOfRecipe.apply {
            appImageHelper(image, ivRecipeDetails)
            tvTitle.text = categoryName
            tvDuration.text = "$duration Minutes"
            tvIngredient.text = ingredient.trim()
            tvSteps.text = steps
            tvCategory.text = categoryID
        }

    }

    private fun getIntentData() {
        if (intent.extras != null) {
            dataOfRecipe = Gson().fromJson<RecipeEntity>(
                intent.getStringExtra("RECIPE_DATA"),
                RecipeEntity::class.java
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnEdit -> {
                openEdit()
            }

            R.id.btnDelete -> {
                onDeletePressed()
            }
        }
    }

    private fun openEdit() {

        startActivity(
            Intent(
                this,
                AddEditActivity::class.java
            ).putExtra(
                "RECIPE_DATA",
                Gson().toJson(dataOfRecipe, RecipeEntity::class.java)
            )
        )
    }

    private fun onDeletePressed() {
        showDialogWith2Buttons(
            this,
            "Are you sure you want to delete ${dataOfRecipe.categoryName.toLowerCase().capitalize()}?",
            "Delete Recipe",
            object : AppDialogs.OnDialogClickListener {
                override fun onPositiveListener() {
                    detailsActivityBrain.removeItemFromDatabase(recipeDatabase, dataOfRecipe)
                    toast("Recipe has been deleted successfully")
                    onBackPressed()
                }

                override fun onNegativeListener() {

                }

            }
        )
    }
}
