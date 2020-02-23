package com.muthu.fourtitude.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object CameraUtils {

    fun getOutputMediaFile(activity: Activity): File? {
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(Date())
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ), "Camera"
        )
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return File(activity.filesDir.toString() + File.separator + "IMG_" + timeStamp + ".jpg")
            }
        }
        return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
    }


    fun saveImageInAppFolder(bitmap: Bitmap, context: Context): String {
        val cw = ContextWrapper(context)
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val file = File(directory, imageFileName)
        if (!file.exists()) {
            Log.e("path is ", file.toString())
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return file.toString()
    }


}