package com.muthu.fourtitude.base

import android.app.Application
import com.muthu.fourtitude.storage.RecipeDatabase

import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class AppApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AppApplication))

        bind() from singleton { RecipeDatabase(context = this@AppApplication) }

    }
}