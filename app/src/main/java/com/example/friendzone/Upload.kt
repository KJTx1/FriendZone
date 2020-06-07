package com.example.friendzone

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_upload.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Upload : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 0
    private val GALLERY_REQUEST_CODE = 1000

    private var isRecording: Boolean = false

    private var isPlaying: Boolean = false

    private var imageUri: Uri? = null

    private var cameraImageFile: File? = null

    private var mediaRecorder: MediaRecorder? = null
    private var audioFileOutput: String? = null

    private var audioDownloadUrl: String? = null

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var storageRef: StorageReference
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        auth = (application as FriendZoneApp).auth

        hideRest()

        btnPost.alpha = .5f;
        btnPost.isClickable = false;

        var groups = arrayOf("group 1", "group 2", "group 3", "group 4", "group 5", "group 6")
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, groups)
        groupSpinner.adapter = adapter

        btnPlay.visibility = View.GONE
        btnPlay.isEnabled = false

        btnDelete.visibility = View.GONE
        btnDelete.isEnabled = false

        btnFromCamera.setOnClickListener {
            imageUri = null
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {

                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        btnFromGallery.setOnClickListener {
            imageUri = null
            checkPermissionForImage()
        }

        btnRecord.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                val permissions = arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, permissions,0)
            } else if (!isRecording) {
                startRecording()
            } else if (isRecording) {
                stopRecording()
            }
        }

        btnPlay.setOnClickListener {
            playRecording()
        }

        btnDelete.setOnClickListener {
            deleteRecording()
        }

        etDescription.addTextChangedListener (object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (etDescription.text.isEmpty()) {
                    btnPost.alpha = .5f
                    btnPost.isClickable = false
                } else {
                    btnPost.alpha = 1f
                    btnPost.isClickable = true
                }
            }
        })

        btnPost.setOnClickListener {
            if (isRecording) {
                stopRecording()
            }

            if ((application as FriendZoneApp).internetManager.isNetworkAvailable(this)) {
                uploadFile()
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        storageRef = FirebaseStorage.getInstance().getReference("groups")
        databaseRef = FirebaseDatabase.getInstance().getReference("groups")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        cameraImageFile?.delete()

        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    showRest()
                    btnFromGallery.visibility = View.GONE

                    val imageBitmap = data.extras?.get("data") as Bitmap
                    btnFromCamera.setImageBitmap(imageBitmap)

                    btnFromCamera.scaleType = ImageView.ScaleType.FIT_CENTER

                    // Move to Post button listener !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    if (isExternalStorageWritable()) {

                        cameraImageFile = File(getExternalFilesDir(null).toString(), "${System.currentTimeMillis()}.jpg")

                        try {
                            val stream: OutputStream = FileOutputStream(cameraImageFile!!)
                            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                            stream.flush()
                            stream.close()

                            imageUri = Uri.fromFile(cameraImageFile)

//                            Log.i("File", imageFile!!.absolutePath)
//                            btnUploadImage.setImageBitmap(BitmapFactory.decodeFile(imageFile.absolutePath))

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            }

            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                    showRest()
                    btnFromCamera.visibility = View.GONE

                    btnFromGallery.scaleX = 1F
                    btnFromGallery.scaleType = ImageView.ScaleType.FIT_CENTER

                    imageUri = data.data
                    btnFromGallery.setImageURI(imageUri)

                }
            }
        }
    }

    private fun startRecording() {

        deleteRecording()

        mediaRecorder = MediaRecorder()
        audioFileOutput = getExternalFilesDir(null).toString() + "/recording_${System.currentTimeMillis()}.mp3"
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mediaRecorder?.setOutputFile(audioFileOutput)


        btnRecord.setImageResource(R.drawable.ic_recording_foreground)
        isRecording = true
        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {

        btnRecord.setImageResource(R.drawable.ic_record_foreground)
        btnPlay.visibility = View.VISIBLE
        btnPlay.isEnabled = true

        btnDelete.visibility = View.VISIBLE
        btnDelete.isEnabled = true

        isRecording = false

        btnPlay.setImageResource(R.drawable.ic_play_foreground)

        try {
//            Set Timeout
            mediaRecorder?.stop()
            mediaRecorder?.release()

            isPlaying = false
            mediaPlayer = MediaPlayer().apply {
                setDataSource(audioFileOutput)
                prepare()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playRecording() {
        if (audioFileOutput != null) {

            if (!isPlaying) {
                isPlaying = true
                mediaPlayer?.start()
                btnPlay.setImageResource(R.drawable.ic_pause_foreground)
            } else {
                isPlaying = false
                mediaPlayer?.pause()
                btnPlay.setImageResource(R.drawable.ic_play_foreground)
            }

            mediaPlayer?.setOnCompletionListener {
                btnPlay.setImageResource(R.drawable.ic_play_foreground)
                isPlaying = false
            }
        }
    }

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    private fun deleteRecording() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
        }

        if (audioFileOutput != null) {
            File(audioFileOutput!!).delete()
            audioFileOutput = null
        }

        btnPlay.visibility = View.GONE
        btnPlay.isEnabled = false

        btnDelete.visibility = View.GONE
        btnDelete.isEnabled = false

    }

    private fun showRest() {
        clAudioBar.visibility = View.VISIBLE
        llPostDetails.visibility = View.VISIBLE
    }

    private fun hideRest() {
        clAudioBar.visibility = View.GONE
        llPostDetails.visibility = View.GONE
    }

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, 1001)
                requestPermissions(permissionCoarse, 1002)
            } else {
                pickImageFromGallery()
            }
        }
    }

//    private fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val nw      = connectivityManager.activeNetwork ?: return false
//            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
//            return when {
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                //for other device how are able to connect with Ethernet
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                //for check internet over Bluetooth
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
//                else -> false
//            }
//        } else {
//            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
//            return nwInfo.isConnected
//        }
//    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun getFileExtension(uri: Uri): String? {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadFile() {

        if (imageUri != null) {
            var extension = getFileExtension(imageUri!!)
            if (extension == null) {
                extension = "jpg"
            }

            var fileReference = storageRef.child(
                groupSpinner.selectedItem.toString() + '/' + System.currentTimeMillis()
                    .toString() + '.' + extension
            )

            fileReference.putFile(imageUri!!)
                .addOnSuccessListener { task ->

                    task.storage.downloadUrl
                        .addOnCompleteListener {

                            val currentTime = System.currentTimeMillis()

                            if (audioFileOutput == null) {
                                val upload = UploadManager(
                                    auth.currentUser?.displayName.toString(),
                                    currentTime,
                                    groupSpinner.selectedItem.toString(),
                                    etDescription.text.toString(),
                                    auth.currentUser?.email.toString(),
                                    it.result.toString(),
                                    "",
                                    auth.currentUser?.uid.toString(),
                                    emptyList()
                                )

                                databaseRef.child(groupSpinner.selectedItem.toString() + '/' + currentTime.toString() + auth.currentUser?.uid.toString())
                                    .setValue(upload)

                                Toast.makeText(this, "Upload Successful!", Toast.LENGTH_SHORT)
                                    .show()

                                finish()
                            } else {
                                uploadAudio(it.result.toString(), currentTime)
                            }
                        }


//                databaseRef.child(auth.currentUser?.email.toString() + '/' + currentTime.toString()).setValue(upload)

//                var upload = UploadManager().upload(auth.currentUser?.email.toString(), it.metadata?.reference?.downloadUrl.toString())
//                var uploadID = databaseRef.push().key
//                if (uploadID != null) {
////                    databaseRef.child(uploadID).setValue(upload)
//                    databaseRef.setValue(upload)
//                    Log.i("XXX", auth.currentUser?.email.toString())
//                    Log.i("XXX", it.metadata?.reference?.downloadUrl.toString())
//                }
                }
                .addOnFailureListener {
                    btnPost.alpha = 1f
                    btnPost.isClickable = true
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {
                    btnPost.alpha = .5f;
                    btnPost.isClickable = false;
                }
        }
    }

    private fun uploadAudio(imageUrl: String, currentTime: Long) {
        val recordingUrl = Uri.fromFile(File(audioFileOutput!!))

        val fileReference = storageRef.child(
            groupSpinner.selectedItem.toString() + '/' + currentTime + ".mp3"
        )

        fileReference.putFile(recordingUrl!!)
            .addOnSuccessListener {task ->
                task.storage.downloadUrl
                    .addOnCompleteListener {
                        audioDownloadUrl = it.result.toString()

                        val upload = UploadManager(
                            auth.currentUser?.displayName.toString(),
                            currentTime,
                            groupSpinner.selectedItem.toString(),
                            etDescription.text.toString(),
                            auth.currentUser?.email.toString(),
                            imageUrl,
                            it.result.toString(),
                            auth.currentUser?.uid.toString(),
                            emptyList()
                        )

                        databaseRef.child(groupSpinner.selectedItem.toString() + '/' + currentTime.toString() + auth.currentUser?.uid.toString())
                            .setValue(upload)

                        Toast.makeText(this, "Upload Successful!", Toast.LENGTH_SHORT).show()

                        finish()

                    }
                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                btnPost.alpha = 1f
                btnPost.isClickable = true
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

    }

}
