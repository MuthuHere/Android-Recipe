package com.muthu.fourtitude.ui.adapter

import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.muthu.fourtitude.R
import com.muthu.fourtitude.storage.RecipeEntity
import com.muthu.fourtitude.ui.details.RecipeDetailsActivity
import com.muthu.fourtitude.utils.ImageHelper.appImageHelper
import kotlinx.android.synthetic.main.item_recipe_list.view.*


class RecipeListAdapter(
    private val data: List<RecipeEntity?>?
) : RecyclerView.Adapter<RecipeListAdapter.RecipeVH>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_list, parent, false)

        return RecipeVH(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: RecipeVH, position: Int) {
        holder.onBind(data?.get(position)!!)
        holder.itemView.setOnClickListener {
            holder.itemView.context.startActivity(
                Intent(
                    holder.itemView.context,
                    RecipeDetailsActivity::class.java
                ).putExtra(
                    "RECIPE_DATA",
                    Gson().toJson(data[position], RecipeEntity::class.java)
                )
            )

        }
    }

    inner class RecipeVH(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        fun onBind(recipe: RecipeEntity?) {
            recipe?.let {
                view.apply {
                    appImageHelper(it.image, ivRecipe)
                    tvRecipeTitle.text = it.categoryName
                    tvRecipeDuration.text = "${it.duration} Minutes"
                }
            }
        }
    }

}