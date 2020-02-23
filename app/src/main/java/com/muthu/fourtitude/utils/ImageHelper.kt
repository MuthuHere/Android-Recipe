package com.muthu.fourtitude.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.muthu.fourtitude.R

object ImageHelper {

    fun appImageHelper(imageUrl: String, iv: ImageView) {

        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))
        Glide.with(iv.context)
            .load(imageUrl.trim())
            .placeholder(R.drawable.ic_bg_placeholder)
            .apply(requestOptions)
            .into(iv)


    }
}