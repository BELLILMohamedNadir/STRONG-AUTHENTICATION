package com.example.tp.ui.fragment.tp1

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import com.example.tp.R
import com.example.tp.databinding.FragmentShowEncryptedImageBinding
import com.example.tp.ui.viewModel.ImageViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.buffer
import okio.source
import java.io.*


class ShowEncryptedImageFragment : Fragment() {

    lateinit var binding:FragmentShowEncryptedImageBinding
    private val imageViewModel= ImageViewModel()
    var text:String=""
    lateinit var bitmap:Bitmap
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentShowEncryptedImageBinding.inflate(inflater,container,false)
        text=arguments?.getString("text","").toString()
        val uri:Uri=Uri.parse(arguments?.getString("uri"))
        uploadImage(uri)

        ImageViewModel.liveEncryptedImage.observe(this){
            setByteArrayAsImage(it.bytes())
        }

        binding.imgDownload.setOnClickListener {
            binding.imgDownload.visibility=View.GONE
            binding.progressEncryption.visibility=View.VISIBLE
            saveBitmapToGallery(bitmap)
        }

        return binding.root
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
                        RequestBody.create("text/plain".toMediaTypeOrNull(),text)
                    imageViewModel.uploadEncryptedImage(body,requestBody)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun setByteArrayAsImage(byteArray: ByteArray) {
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        binding.progressEncryptedImage.visibility=View.GONE
        binding.cardEncryptedImage.visibility=View.VISIBLE
        binding.imgDownload.visibility=View.VISIBLE
        binding.imgEncryptedImage.setImageBitmap(bitmap)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveBitmapToGallery(bitmap: Bitmap) {
        val imageOutStream: OutputStream?

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "encrypted_image.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val contentResolver = activity?.applicationContext?.contentResolver
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

        try {
            val imageUri = contentResolver?.insert(collection, values)
            imageOutStream = imageUri?.let { contentResolver.openOutputStream(it) }

            if (imageOutStream != null) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream)
                imageOutStream.close()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver?.update(imageUri!!, values, null, null)
            }
            binding.imgDownload.isEnabled=false
            binding.imgDownload.setImageResource(R.drawable.ic_check)
            binding.progressEncryption.visibility=View.GONE
            binding.imgDownload.visibility=View.VISIBLE
            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
            binding.progressEncryption.visibility=View.GONE
            binding.imgDownload.visibility=View.VISIBLE
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }



}