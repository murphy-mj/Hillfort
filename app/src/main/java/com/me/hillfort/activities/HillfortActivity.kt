package com.me.hillfort.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.view.View
import android.widget.Button
import android.widget.DatePicker
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
import java.text.SimpleDateFormat
import java.util.logging.Logger


class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()

    lateinit var app: MainApp

    val IMAGE_REQUEST = 5
    val LOCATION_REQUEST = 3
    private var btn: Button? = null
    private var imageview: ImageView? = null
    private val GALLERY = 1
    private val CAMERA = 2


    var fileUri: Uri? = null

    //   var location:Location = { if (location?.lat == 0.0 && location?.lng == 0.0 ){ location = Location(52.245696, -7.139102, 15f) }}
    var location = Location(52.245696, -7.139102, 15f)
    var markerName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")


        app = application as MainApp
        var edit = false
        var del = false
        var visit: Boolean = false

        //chooseImage.setOnClickListener { askCameraPermission() }
        chooseImage.setOnClickListener {showPictureDialog() }




        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!

            chooseImage.setText(R.string.button_selectImage)
            hillfortLocation.setText(R.string.button_updateLocation)
            btnAdd.setText(R.string.button_updateHillfort)
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image != null) {
                chooseImage.setText(R.string.button_selectImage)
                toast(R.string.hint_hillfortImage)
            }
            location_lat.setText(hillfort.lat.toString())
            location_lat.setText(hillfort.lng.toString())
            hillfortToggleButton.setChecked(hillfort.visit_yn)
            date_text_view.setText(hillfort.visit_date)


            btnAdd.setText(R.string.button_saveHillfort)
            btnDel.setText(R.string.button_deleteHillfort)
        }



        btnAdd.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            var visit:Boolean = hillfortToggleButton.isChecked()
            toast("visited is ${visit}")
            hillfort.visit_yn = visit
            hillfort.visit_date = date_text_view.text.toString()
            hillfort.lat = location_lat.text as Double
            hillfort.lng = location_lng.text as Double
          //  hillfort.visit_date = date_text_view.text.toString()
            if (hillfort.title.isEmpty()) {
                toast(R.string.hint_hillfortTitle)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }
            }
            info("add Button Pressed: $hillfortTitle")
            toast(R.string.hint_hillfortTitle)
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }

        btnDel.setOnClickListener() {
            if (edit == false) {
                finish()
            } else {
                app.hillforts.remove(hillfort.copy())
                info("delete Button Pressed: $hillfortTitle")
                toast(R.string.toast_hillfortDeleted)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }

        }


        date_text_view.setOnClickListener{
            val today = Calendar.getInstance()
            DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, yyyy: Int, mm: Int, dd: Int) {
                    val selected = Date(yyyy-1900, mm, dd) // Create a date object with offset.
                    date_text_view.text = selected.toString() // Display the selected date, or do whatever.
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show()
        }


        hillfortLocation.setOnClickListener {
            validatePermission()
            startActivity (intentFor<MapsActivity>().putExtra("name", hillfort.title))

         //   startActivityForResult<MapsActivity>(0)
         //   val location = Location(location_lat.text as Double, location_lng.text as Double, 15f)

          //  startActivityForResult(intentFor<MapsActivity>()
          //      .putExtra("location", location), LOCATION_REQUEST)
          //  val location = Location(52.245696, -7.139102, 15f)
         //   startActivity (intentFor<MapsActivity>().putExtra("location", location))
        }


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
            R.id.item_delete -> {

                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    toast(hillfort.image.toString())
                }}
            CAMERA -> {
                    if (data != null) {
                        hillfort.image = data.getData().toString()
                        toast(hillfort.image.toString())
                        hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                       // val thumbnail = data!!.extras!!.get("data") as Bitmap
                       // imageview!!.setImageBitmap(thumbnail)
                      //  hillfortImage.setImageBitmap(thumbnail)
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
         showImagePicker(this, GALLERY)

        //val galleryIntent = Intent(Intent.ACTION_PICK,
        //    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // val galleryIntent = Intent()
        // galleryIntent.type = "image/*"
        // galleryIntent.action = Intent.ACTION_OPEN_DOCUMENT
        // galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
       // startActivityForResult(galleryIntent, GALLERY)
     }


     fun takePhotoFromCamera() {
         askCameraPermission()
       // val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
       // startActivityForResult(intent, CAMERA)
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
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, CAMERA)
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


    fun validatePermission(){
        toast("ask permission for fine location")
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {/* ... */
                    if(report.areAllPermissionsGranted()){
                        //once permissions are granted, launch the camera
                       // launchCamera()
                    }else{
                        toast("All permissions need to be granted to use fine location")
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



}
