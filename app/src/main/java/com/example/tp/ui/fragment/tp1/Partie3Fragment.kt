package com.example.tp.ui.fragment.tp1

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentPartie3Binding


class Partie3Fragment : Fragment() {
    private lateinit var binding: FragmentPartie3Binding
    var uri:Uri?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPartie3Binding.inflate(inflater,container,false)

        val arl = registerForActivityResult<Array<String>, Uri>(
            ActivityResultContracts.OpenDocument()
        ) {
            uri=it
            if (it!=null)
                binding.imgEncrypted.setImageURI(it)
        }

        binding.imgEncrypted.setOnClickListener {
            arl.launch(arrayOf("image/*"))
        }
        binding.btnSendEncryption.setOnClickListener {
            if (uri==null){
                Toast.makeText(context,"Choose an image",Toast.LENGTH_SHORT).show()
            }else{
                if (binding.edtTextEncryption.text.toString().isEmpty()){
                    Toast.makeText(context,"Add text",Toast.LENGTH_SHORT).show()
                }else{
                    val bundle=Bundle()
                    bundle.putString("uri","$uri")
                    bundle.putString("text", binding.edtTextEncryption.text.toString())
                    findNavController().navigate(R.id.showEncryptedImageFragment,bundle)
                }
            }
        }


        return binding.root
    }



}