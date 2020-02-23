package com.muthu.fourtitude.ui.addedit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.muthu.fourtitude.R
import com.muthu.fourtitude.base.BaseActivity
import com.muthu.fourtitude.storage.RecipeEntity
import com.muthu.fourtitude.ui.home.MainActivity
import com.muthu.fourtitude.utils.AppDialogs
import com.muthu.fourtitude.utils.AppDialogs.showDialogWith2Buttons
import com.muthu.fourtitude.utils.AppMessages.toast
import com.muthu.fourtitude.utils.CameraUtils.getOutputMediaFile
import com.muthu.fourtitude.utils.CameraUtils.saveImageInAppFolder
import com.muthu.fourtitude.utils.ImageHelper.appImageHelper
import kotlinx.android.synthetic.main.activity_add_edit.*
import kotlinx.android.synthetic.main.content_add_edit.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.*


class AddEditActivity : BaseActivity() {


    private lateinit var dataOfRecipe: RecipeEntity
    private var imageUriFromCamera: Uri? = null
    private var imagePath = ""
    private var isEdit = false
    private var selectedCategory = "All Category"

    private val brain = AddEditActivityBrain()

    companion object {
        const val REQUEST_PERMISSION_SETTINGS = 10067
        const val IMAGE_CAPTURE = 10068
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getIntentData()
        initClickListener()
        spinnerCategoryListener()

    }

    private fun spinnerCategoryListener() {

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = spinnerCategory.selectedItem.toString()
            }

        }
    }

    private fun initClickListener() {

        ivCapture.setOnClickListener(this)
        btnSave.setOnClickListener(this)
    }

    private fun preFillData() {
        dataOfRecipe.apply {
            etRecipeName.setText(categoryName)
            etRecipeDuration.setText("$duration")
            etRecipeIngredient.setText("$ingredient")
            etRecipeSteps.setText("$steps")

            appImageHelper(image, ivRecipe)

            //pre fill for update without any change
            selectedCategory = categoryID
            imagePath = image

            //spinner
            val stringArray = resources.getStringArray(R.array.category_type)
            stringArray.forEachIndexed { index, s ->
                if (s == categoryID) {
                    spinnerCategory.setSelection(index)
                    return
                }
            }


        }
    }


    private fun getIntentData() {
        if (intent.extras != null) {
            dataOfRecipe = Gson().fromJson<RecipeEntity>(
                intent.getStringExtra("RECIPE_DATA"),
                RecipeEntity::class.java
            )
            isEdit = true
            //title
            title = "Edit Recipe"
            //edit
            preFillData()

        }
    }

    override fun onClick(p0: View?) {
        super.onClick(p0)

        when (p0?.id) {
            R.id.ivCapture -> {
                getCameraPermission()
            }

            R.id.btnSave -> {

                validateFields()
            }
        }

    }

    private fun validateFields() {

        clearError()

        if (imagePath.isEmpty()) {
            toast("Please capture Recipe's image")
            return
        }

        if (TextUtils.isEmpty(etRecipeName.text)) {
            etRecipeName.requestFocus()
            etRecipeName.error = "Please enter Recipe Name"
            return
        }


        if (TextUtils.isEmpty(etRecipeDuration.text)) {
            etRecipeDuration.requestFocus()
            etRecipeDuration.error = "Please enter duration for cooking"
            return
        }

        if (TextUtils.isEmpty(etRecipeIngredient.text)) {
            etRecipeIngredient.requestFocus()
            etRecipeIngredient.error = "Please enter Recipe's ingredient"
            return
        }
        if (TextUtils.isEmpty(etRecipeSteps.text)) {
            etRecipeSteps.requestFocus()
            etRecipeSteps.error = "Please enter steps"
            return
        }

        if (isEdit) {
            brain.updateRecipe(
                dataOfRecipe.id,
                recipeDatabase, RecipeEntity(
                    categoryName = etRecipeName.text.toString(),
                    image = imagePath,
                    duration = etRecipeDuration.text.toString(),
                    ingredient = etRecipeIngredient.text.toString(),
                    categoryID = selectedCategory,
                    steps = etRecipeSteps.text.toString()
                )
            )

            toast("Updated successfully!")

            clearStackAndGoToMainActivity()
        } else {
            brain.createRecipe(
                recipeDatabase, RecipeEntity(
                    categoryName = etRecipeName.text.toString(),
                    image = imagePath,
                    duration = etRecipeDuration.text.toString(),
                    ingredient = etRecipeIngredient.text.toString(),
                    categoryID = selectedCategory,
                    steps = etRecipeSteps.text.toString()
                )
            )

            toast("Added Successfully!")
            clearStackAndGoToMainActivity()
        }


    }

    private fun clearStackAndGoToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun clearError() {
        etRecipeName.error = null
        etRecipeDuration.error = null
        etRecipeIngredient.error = null
        etRecipeSteps.error = null
    }

    private fun getCameraPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        //open camera
                        openCamera()

                    } else if (report.isAnyPermissionPermanentlyDenied) {
                        showDialogWith2Buttons(this@AddEditActivity,
                            "This app needs permission to use this feature. You can grant them in app settings.",
                            "Need Permission",
                            object : AppDialogs.OnDialogClickListener {
                                override fun onPositiveListener() {
                                    openSettings()
                                }

                                override fun onNegativeListener() {
                                }

                            }
                        )
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_PERMISSION_SETTINGS -> {
                    getCameraPermission()
                }

                IMAGE_CAPTURE -> {
                    val bitmap: Bitmap
                    if (imageUriFromCamera != null) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(
                                Objects.requireNonNull(this).contentResolver,
                                imageUriFromCamera
                            )
                            ivRecipe.setImageBitmap(bitmap)
                            imagePath = saveImageInAppFolder(bitmap, this@AddEditActivity)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }


    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, REQUEST_PERMISSION_SETTINGS)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUriFromCamera = Uri.fromFile(getOutputMediaFile(this))
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        } else {
            val file = File(Uri.fromFile(getOutputMediaFile(this)).path)
            imageUriFromCamera = FileProvider.getUriForFile(
                applicationContext,
                applicationContext.packageName + ".provider",
                file
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriFromCamera)
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        if (intent.resolveActivity(applicationContext.packageManager) != null) {
            startActivityForResult(
                intent,
                IMAGE_CAPTURE
            )
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
