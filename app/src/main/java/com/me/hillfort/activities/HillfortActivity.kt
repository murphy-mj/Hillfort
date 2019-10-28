package com.me.hillfort.activities

import android.Manifest
//import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import kotlinx.android.synthetic.main.activity_hillfort.*
import com.me.hillfort.activities.MapsActivity
import com.me.hillfort.R
import com.me.hillfort.helpers.readImage
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.helpers.showImagePicker
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.Location
import com.me.hillfort.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.hillfortDescription
import kotlinx.android.synthetic.main.activity_hillfort.hillfortTitle
import kotlinx.android.synthetic.main.activity_hillfort.hillfortImage
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener





class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    private var btn: Button? = null
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2

    val TAKE_PHOTO_REQUEST: Int = 2
    val PICK_PHOTO_REQUEST: Int = 1
    var fileUri: Uri? = null

    //   var location:Location = { if (location?.lat == 0.0 && location?.lng == 0.0 ){ location = Location(52.245696, -7.139102, 15f) }}
    var location = Location(52.245696, -7.139102, 15f)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")

        app = application as MainApp
        var edit = false

        chooseImage.setOnClickListener { showPictureDialog() }


        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image == null) {
                chooseImage.setText(R.string.change_hillfort_image)
            }
            btnAdd.setText(R.string.save_hillfort)
        }

        //else {
        //      hillfort.title = hillfortTitle.text.toString()
        //      hillfort.description = hillfortDescription.text.toString()
        //      hillfort.image = hillfortImage.toString()
        //}

        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.image = hillfortImage.toString()
            if (hillfort.image.isEmpty()) {
                toast(R.string.enter_hillfort_title)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }
            }
            info("add Button Pressed: $hillfortTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

       // chooseImage.setOnClickListener {
       //     startActivity (intentFor<ImageCaptureActivity>())
          //  showImagePicker(this, IMAGE_REQUEST)
       // }

        //  placemarkLocation.setOnClickListener {
        //   info ("Set Location Pressed")
        //    toast("set Location passed")
        //    startActivity (intentFor<MapsActivity>())
        //  }

   //     hillfortLocation.setOnClickListener {
   //         startActivityForResult(intentFor<MapsActivity>()
    //            .putExtra("location", location), LOCATION_REQUEST)
            // val location = Location(52.245696, -7.139102, 15f)
            // startActivity (intentFor<MapsActivity>().putExtra("location", location))
    //    }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
           // IMAGE_REQUEST -> {
           //     if (data != null) {
           //         hillfort.image = data.getData().toString()
           //         hillfortImage.setImageBitmap(readImage(this, resultCode, data))
           //         toast(hillfort.image.toString())
           //     }}
            CAMERA -> {
                    if (data != null) {
                        hillfort.image = data.getData().toString()
                        toast(hillfort.image.toString())
                        //hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                        val thumbnail = data!!.extras!!.get("data") as Bitmap
                       // imageview!!.setImageBitmap(thumbnail)
                        hillfortImage.setImageBitmap(thumbnail)
                       // hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                       // saveImage(thumbnail)
                     //   Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    }
                }

            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras?.getParcelable<Location>("location")!!
                }
            }
        }
    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

     fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

     fun takePhotoFromCamera() {
      //  askCameraPermission()
        launchCamera()
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
           // intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
            //        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    fun askCameraPermission(){
        toast("ask permission")
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if(report.areAllPermissionsGranted()){
                        //once permissions are granted, launch the camera
                        launchCamera()
                    }else{
                        toast("All permissions need to be granted to take photo")
                      //  Toast.makeText(this, "All permissions need to be granted to take photo", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {/* ... */
                    //show alert dialog with permission options
                    AlertDialog.Builder(this@HillfortActivity)
                        .setTitle(
                            "title")
                        .setMessage(
                            "message")
                        .setNegativeButton(
                            android.R.string.cancel,
                            { dialog, _ ->
                                dialog.dismiss()
                                token?.cancelPermissionRequest()
                            })
                        .setPositiveButton(android.R.string.ok,
                            { dialog, _ ->
                                dialog.dismiss()
                                token?.continuePermissionRequest()
                            })
                        .setOnDismissListener({
                            token?.cancelPermissionRequest() })
                        .show()
                }

            }).check()

    }










        fun saveImage(myBitmap: Bitmap):String {
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
            val wallpaperDirectory = File(
                (Environment.getExternalStorageDirectory()).toString() + IMAGE_DIRECTORY)
            // have the object build the directory structure, if needed.
            Log.d("fee",wallpaperDirectory.toString())
            if (!wallpaperDirectory.exists())
            {

                wallpaperDirectory.mkdirs()
            }

            try
            {
                Log.d("heel",wallpaperDirectory.toString())
                val f = File(wallpaperDirectory, ((Calendar.getInstance()
                    .getTimeInMillis()).toString() + ".jpg"))
                f.createNewFile()
                val fo = FileOutputStream(f)
                fo.write(bytes.toByteArray())
                MediaScannerConnection.scanFile(this,
                    arrayOf(f.getPath()),
                    arrayOf("image/jpeg"), null)
                fo.close()
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

                return f.getAbsolutePath()
            }
            catch (e1: IOException) {
                e1.printStackTrace()
            }

            return ""
        }

        companion object {
            private val IMAGE_DIRECTORY = "/demonuts"
        }


}
