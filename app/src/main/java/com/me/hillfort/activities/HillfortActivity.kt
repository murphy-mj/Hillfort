package com.me.hillfort.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
//import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_hillfort.*
import com.me.hillfort.activities.MapsActivity
import com.me.hillfort.R
import com.me.hillfort.helpers.readImage
import com.me.hillfort.helpers.readImageFromPath
import com.me.hillfort.helpers.showImagePicker
import com.me.hillfort.main.MainApp
import com.me.hillfort.models.Location
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.activities.ImageCaptureActivity
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
import kotlinx.android.synthetic.main.activity_hillfort.toolbarAdd
import kotlinx.android.synthetic.main.activity_hillfort2.*
import kotlinx.android.synthetic.main.activity_maps.*
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

// add note for commit
    var fileUri: Uri? = null

    //   var location:Location = { if (location?.lat == 0.0 && location?.lng == 0.0 ){ location = Location(52.245696, -7.139102, 15f) }}
    var location = Location(52.245696, -7.139102, 15f)
    var markerName:String = ""
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort2)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        info("Hillfort Activity started..")


        app = application as MainApp
        var del = false
        var visit: Boolean = false

        //chooseImage.setOnClickListener { askCameraPermission() }





        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = intent.extras?.getParcelable<HillfortModel>("hillfort_edit")!!

            chooseImage.setText(R.string.button_selectImage)
            hillfortLocation2.setText(R.string.button_updateLocation)
            btnAdd2.setText(R.string.button_updateHillfort)
            hillfortTitle.setText(hillfort.title)
            hillfortDescription.setText(hillfort.description)
            Glide.with(this).load(hillfort.image).into(hillfortImage)
          //  hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            if (hillfort.image == null) {
                chooseImage.setText(R.string.button_selectImage)
                toast(R.string.hint_hillfortImage)
            }

           // location_lat.setText(hillfort.lat.toString())
           // location_lat.setText(hillfort.lng.toString())
            hillfortToggleButton2.setChecked(hillfort.visit_yn)
            date_text_view2.setText(hillfort.visit_date)

            btnAdd2.setText(R.string.button_updateHillfort)
            btnDel2.setText(R.string.button_deleteHillfort)
        }


        btnAdd2.setOnClickListener() {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = hillfortDescription.text.toString()
            hillfort.image = hillfortImage.toString()
            var visit:Boolean = hillfortToggleButton2.isChecked()
            toast("visited is ${visit}")
            hillfort.visit_yn = visit
            hillfort.visit_date = date_text_view2.text.toString()
         //   hillfort.lat = location_lat.text as Double
         //   hillfort.lng = location_lng.text as Double
          //  hillfort.visit_date = date_text_view.text.toString()

          //  if (hillfort.title.isEmpty()) {
           //     toast(R.string.hint_hillfortTitle)
           // } else {
            if (edit) {
                    // an existing edited version hillfort for saving
                    app.hillforts.update(hillfort.copy())
                if ((hillfort.image.length) > 0 && (hillfort.image[0] != 'h')) {
                    updateImage(hillfort)
                }

                    app.database.child("user-hillforts").child(app.auth.currentUser!!.uid).child(hillfort.uid.toString()).setValue(hillfort)
                    app.database.child("hillforts").child(hillfort.uid.toString()).setValue(hillfort)

            } else {
                    // new hillfort being presented storage
                    // store data to firestore database
                    info("Firebase DB Reference : $app.database")
                    toast("ADDING hillfort to ${app.auth.currentUser!!.uid}")
                    val uid = app.auth.currentUser!!.uid
                    val key = app.database.child("hillforts").push().key
                    if (key == null) {
                        info("Firebase Error : Key Empty")
                        //return
                    } else {
                        hillfort.uid = key
                    }
                    val hillfortValues = hillfort.toMap()
                    val childUpdates = HashMap<String, Any>()
                    childUpdates["/hillforts/$key"] = hillfortValues
                    childUpdates["/user-hillforts/$uid/$key"] = hillfortValues
                    app.database.updateChildren(childUpdates)
                    //  hideLoader(loader)

                    // keeping json data for ref
                    app.hillforts.create(hillfort.copy())
                }
           // }
            info("add Button Pressed: $hillfortTitle")
            toast(R.string.hint_hillfortTitle)
            setResult(AppCompatActivity.RESULT_OK)
            finish()
        }







        btnDel2.setOnClickListener() {
            if (edit == false) {
                finish()
            } else {
                app.hillforts.remove(hillfort.copy())

                val userId = app.auth.currentUser!!.uid

                app.database.child("hillforts").child( hillfort.uid!!)
                    .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.ref.removeValue()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                info("Firebase Hillfort error : ${error.message}")
                            }
                        })

                app.database.child("user-hillforts").child(userId).child( hillfort.uid!!)
                    .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                snapshot.ref.removeValue()
                            }
                            override fun onCancelled(error: DatabaseError) {
                                info("Firebase Donation error : ${error.message}")
                            }
                        })

                info("delete Button Pressed: $hillfortTitle")
                toast(R.string.toast_hillfortDeleted)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }

        }

        chooseImage.setOnClickListener {showPictureDialog() }


        date_text_view2.setOnClickListener{
            val today = Calendar.getInstance()
            DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, yyyy: Int, mm: Int, dd: Int) {
                    val selected = Date(yyyy-1900, mm, dd) // Create a date object with offset.
                    date_text_view2.text = selected.toString() // Display the selected date, or do whatever.
                }
            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show()
        }


        hillfortLocation2.setOnClickListener {
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
                if (edit == false) {
                    finish()
                } else {
                    app.hillforts.remove(hillfort.copy())

                    toast(R.string.toast_hillfortDeleted)
                    setResult(AppCompatActivity.RESULT_OK)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

var photoselectedURI :Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GALLERY -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                 //   hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
                    updateImage(hillfort)
                //    toast("from GALLERY result " + hillfort.image.toString())
                //    val photoselectedURI = data.data
                //    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,photoselectedURI)
                //    val bitmapDrawable = BitmapDrawable(bitmap)
                //    hillfortImage.setBackgroundDrawable(bitmapDrawable)


                }}
            CAMERA -> {
                    if (data != null) {
                        hillfort.image = data.getData().toString()
                        updateImage(hillfort)
                        val uri = data.data
                        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                        val bitmapDrawable = BitmapDrawable(bitmap)
                        hillfortImage.setBackgroundDrawable(bitmapDrawable)

                        toast(hillfort.image.toString())
                      //  hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                 //       hillfortImage.setImageBitmap(readImageFromPath(this, hillfort.image))
                       // val thumbnail = data!!.extras!!.get("data") as Bitmap
                       // imageview!!.setImageBitmap(thumbnail)
                       // hillfortImage.setImageBitmap(thumbnail)
                       // hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                       // saveImage(thumbnail: Bitmap)

                        Toast.makeText(this, "Image Saved!", Toast.LENGTH_SHORT).show()
                    }
                }

            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras?.getParcelable<Location>("location")!!
                }
            }
        }
    }


    // select image button
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

        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
       //  galleryIntent = Intent()
         galleryIntent.type = "image/*"
         galleryIntent.action = Intent.ACTION_OPEN_DOCUMENT
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(galleryIntent, GALLERY)
     }


     fun takePhotoFromCamera() {
         askCameraPermission()
    //    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
   //     startActivityForResult(intent, CAMERA)
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


    fun updateImage(hillfort: HillfortModel) {
        if (hillfort.image != "") {
            val fileName = File(hillfort.image)
            val imageName = fileName.getName()

            var imageRef = app.storage.child(app.auth.currentUser!!.uid + '/' + imageName)
            val baos = ByteArrayOutputStream()
            val bitmap = readImageFromPath(this, hillfort.image)

            bitmap?.let {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    println(it.message)
                }.addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        hillfort.image = it.toString()
                        app.database.child("user-hillforts").child(app.auth.currentUser!!.uid).child(hillfort.uid.toString()).setValue(hillfort)
                        app.database.child("hillforts").child(hillfort.uid.toString()).setValue(hillfort)

                       // app.database.child("user-hillforts").child(app.auth.currentUser!!.uid).child("hillforts").child(hillfort.uid.toString()).setValue(hillfort)
                    }
                }
            }
        }
    }
    fun locationUpdate(location: Location) {
        hillfort.location = location
        hillfort.location.zoom = 15f
 //       map?.clear()
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.location.lat, hillfort.location.lng))
 //       map?.addMarker(options)
 //       map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.location.lat, hillfort.location.lng), hillfort.location.zoom))
 //       view?.showLocation(hillfort.location)
    }

}
