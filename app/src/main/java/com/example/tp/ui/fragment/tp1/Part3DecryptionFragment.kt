package com.example.tp.ui.fragment.tp1

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.example.tp.R
import com.example.tp.databinding.FragmentPart3DecryptionBinding
import com.example.tp.ui.viewModel.ImageViewModel
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.buffer
import okio.source
import org.json.JSONObject
import java.io.IOException


class Part3DecryptionFragment : Fragment() {


    lateinit var binding:FragmentPart3DecryptionBinding
    private val imageViewModel =ImageViewModel()
    private var uri:Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentPart3DecryptionBinding.inflate(inflater,container,false)

        val arl = registerForActivityResult<Array<String>, Uri>(
        ActivityResultContracts.OpenDocument()
        ) {
            uri=it
            binding.txtTextDecryption.text="Decryption"
            if (it!=null)
                binding.imgDecryption.setImageURI(it)
            else
                binding.imgDecryption.setImageResource(R.drawable.ic_add)
        }

        binding.imgDecryption.setOnClickListener {
            arl.launch(arrayOf("image/png"))
        }

        binding.btnSendDecryption.setOnClickListener {
            if (uri==null){
                Toast.makeText(context,"Choose an image",Toast.LENGTH_SHORT).show()
            }else{
                binding.btnSendDecryption.visibility=View.GONE
                binding.progressDecryption.visibility=View.VISIBLE
                uploadImage(uri!!)
            }
        }

        ImageViewModel.liveDecryptedImage.observe(this){
            if (it.isSuccessful){
                binding.progressDecryption.visibility=View.GONE
                binding.btnSendDecryption.visibility=View.VISIBLE
                binding.txtTextDecryption.text=getMessage(it)
            }else
                Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    private fun getMessage(it: retrofit2.Response<JsonObject>?): String {
        val jsonString = it?.body().toString()
        val jsonObject = JSONObject(jsonString)
        return jsonObject.getString("message")
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
                        "image/png".toMediaTypeOrNull(),
                        bufferedSource.readByteString()
                    )
                    val body: MultipartBody.Part =
                        MultipartBody.Part.createFormData("image", file.name, requestFile)
                    imageViewModel.uploadDecryptedImage(body)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }


}