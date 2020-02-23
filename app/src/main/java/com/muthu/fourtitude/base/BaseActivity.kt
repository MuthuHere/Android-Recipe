package com.muthu.fourtitude.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.muthu.fourtitude.storage.RecipeDatabase

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

open class BaseActivity : AppCompatActivity(), View.OnClickListener, KodeinAware {

    override val kodein by kodein()
    protected val recipeDatabase: RecipeDatabase by instance()


    override fun onClick(p0: View?) {


    }
}