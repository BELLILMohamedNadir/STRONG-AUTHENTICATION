package com.example.tp.ui.fragment.tp3

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.utils.MatrixExt.postRotate
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.connection.Connection
import com.example.tp.data.user.UserEmail
import com.example.tp.databinding.FragmentTp3FaceRecognitionBinding
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.ui.viewModel.ImageViewModel
import com.example.tp.util.STATUS
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.buffer
import okio.source
import java.io.File
import java.io.IOException

class Tp3FaceRecognitionFragment : Fragment() {

    lateinit var binding : FragmentTp3FaceRecognitionBinding
    private var imageCapture:ImageCapture? = null
    private val imageViewModel = ImageViewModel()
    var email = ""
    var publicKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTp3FaceRecognitionBinding.inflate(inflater,container,false)

        backPressed()

        arguments?.let {
            email = it.getString(URL.EMAIL).toString()
            publicKey = it.getString(URL.PUBLIC_KEY).toString()
        }

        checkPermissions()

        observeData()

        binding.btnTake.setOnClickListener {
            if (Connection.isConnected(activity?.applicationContext)){
                ImageViewModel.liveFaceRecognition.value = null
                binding.btnTake.visibility = View.GONE
                binding.progress.visibility = View.VISIBLE
                capturePhoto()
            }else
                Connection.makeToast(activity?.applicationContext)
        }
        return binding.root
    }

    private fun observeData() {
        ImageViewModel.liveFaceRecognition.observe(this){

            if (it != null){
                if (it == URL.OK_200){
                    val bundle = Bundle()
                    bundle.putString(URL.EMAIL, email)
                    bundle.putString(URL.PUBLIC_KEY, publicKey)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.tp3CheckInfoFragment,bundle)
                }else{
                    binding.progress.visibility = View.GONE
                    binding.btnTake.visibility = View.VISIBLE
                    if (it == URL.CODE_422)
                        Toast.makeText(context,"Take a photo again",Toast.LENGTH_SHORT).show()
                    else
                        STATUS.status(it, context, childFragmentManager)
                }
            }
        }
    }

    private fun checkPermissions() {
        if (!hasRequiredPermissions()){
            ActivityCompat.requestPermissions(
                activity!!, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }else{
            startCamera()
        }
    }

    private fun capturePhoto() {
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(File.createTempFile("photo", ".jpg"))
            .build()

        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let {
                        uploadImage(convertFileUriToContentUri(it)!!)
                    }
                }

                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(context,"Photo capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun uploadImage(uri : Uri) {
        if (activity != null) {
            val file = DocumentFile.fromSingleUri(activity!!, uri)
            if (file != null) {
                try {

                    // Open an InputStream to read the contents of the file
                    val inputStream = activity!!.contentResolver.openInputStream(file.uri)

                    // Use Okio library to read the contents of the InputStream into a BufferedSource object
                    val bufferedSource = inputStream!!.source().buffer()
                    val requestFile = RequestBody.create(
                        "image/*".toMediaTypeOrNull(),
                        bufferedSource.readByteString()
                    )
                    val body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    val requestBody: RequestBody =
                        RequestBody.create("text/plain".toMediaTypeOrNull(),email)
                    imageViewModel.faceRecognition(body,requestBody)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun hasRequiredPermissions():Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                activity!!,
                it
            )== PackageManager.PERMISSION_GRANTED
        }
    }

    private fun startCamera(){
        val  cameraProviderFeature= ProcessCameraProvider
            .getInstance(activity!!)
        cameraProviderFeature.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFeature.get()

            val preview = Preview.Builder()
                .build()
                .also {mPreview->
                    mPreview.setSurfaceProvider(
                        binding.previewView.surfaceProvider
                    )
                }
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    activity!!,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }catch (e : java.lang.Exception){
                Log.d(TAG,"start camera failed: ",e)
            }
        }, ContextCompat.getMainExecutor(activity!!))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if(requestCode == REQUEST_CODE_PERMISSIONS){
            if (hasRequiredPermissions()){
                startCamera()
            }else{
                Toast.makeText(context,"Permission not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object{
        const val TAG = "cameraX"
        const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
        const val REQUEST_CODE_PERMISSIONS = 123
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    private fun convertFileUriToContentUri(fileUri: Uri): Uri? {
        val file = File(fileUri.path ?: return null)
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        }

        val resolver: ContentResolver = requireContext().contentResolver
        val contentUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        // Copy the image content to the new content URI
        resolver.openOutputStream(contentUri!!)?.use { output ->
            // Read the Exif orientation from the original file
            val exif = try {
                ExifInterface(file.absolutePath)
            } catch (e: IOException) {
                null
            }

            // Rotate or flip the image based on the Exif orientation
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            BitmapFactory.decodeFile(file.absolutePath, options)

            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            ) ?: ExifInterface.ORIENTATION_UNDEFINED

            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

            val rotatedBitmap = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flipBitmap(bitmap, horizontal = true)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> flipBitmap(bitmap, horizontal = false)
                else -> bitmap
            }

            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        }

        return contentUri
    }

    private fun rotateBitmap(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix().apply {
            postRotate(angle)
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun flipBitmap(source: Bitmap, horizontal: Boolean): Bitmap {
        val matrix = Matrix().apply {
            if (horizontal) {
                postScale(-1f, 1f, source.width / 2f, source.height / 2f)
            } else {
                postScale(1f, -1f, source.width / 2f, source.height / 2f)
            }
        }
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }
    private fun backPressed(){
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                val confirmFragment = ConfirmFragment()
                confirmFragment.show(childFragmentManager, "confirm_fragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    override fun onPause() {
        super.onPause()
        ImageViewModel.liveFaceRecognition.value = null
    }

}