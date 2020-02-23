package com.muthu.fourtitude.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.muthu.fourtitude.R
import com.muthu.fourtitude.base.BaseActivity
import com.muthu.fourtitude.storage.RecipeEntity
import com.muthu.fourtitude.ui.adapter.RecipeListAdapter
import com.muthu.fourtitude.ui.addedit.AddEditActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    private val mainActivityBrain =
        MainActivityBrain()
    private var strSelectedCategory = ""
    private lateinit var listOfRecipe: List<RecipeEntity?>
    private lateinit var tempListOfRecipe: List<RecipeEntity?>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initView()

        syncDataBase()

        clickListeners()

        updateRecipeList()

        hideFabOnScroll()

        addListDivider()

        spinnerListener()

    }

    private fun initView() {
        rvRecipeList.setHasFixedSize(true)
    }


    private fun spinnerListener() {

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                strSelectedCategory = spinnerCategory.selectedItem.toString()
                filterAndUpdate()
            }

        }
    }

    private fun addListDivider() {

        val dividerItemDecoration = DividerItemDecoration(
            rvRecipeList.context,
            androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
        )
        rvRecipeList.addItemDecoration(dividerItemDecoration)
    }

    private fun hideFabOnScroll() {

        rvRecipeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) fabAddNewItem.hide() else if (dy < 0) fabAddNewItem.show()
            }
        })
    }

    private fun updateRecipeList() {
        recipeDatabase.recipeDao().getAllReceipeList().observeForever {

            listOfRecipe = arrayListOf()
            tempListOfRecipe = arrayListOf()
            listOfRecipe = it!!
            tempListOfRecipe = it

            if (strSelectedCategory.isEmpty()) {
                rvRecipeList.adapter = RecipeListAdapter(tempListOfRecipe)
            } else {
                filterAndUpdate()
            }


        }
    }

    private fun filterAndUpdate() {
        tempListOfRecipe = if (strSelectedCategory == "All Category") {
            listOfRecipe
        } else {
            listOfRecipe.filter { s ->
                s?.categoryID == strSelectedCategory
            }
        }

        rvRecipeList.removeAllViews()
        rvRecipeList.adapter = RecipeListAdapter(tempListOfRecipe)
        rvRecipeList.adapter?.notifyDataSetChanged()
    }

    private fun syncDataBase() {
        mainActivityBrain.syncRecipe(recipeDatabase, this)
    }

    private fun clickListeners() {

        fabAddNewItem.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddEditActivity::class.java))

        }
    }

}
