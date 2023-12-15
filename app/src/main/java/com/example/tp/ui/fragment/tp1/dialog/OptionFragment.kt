package com.example.tp.ui.fragment.tp1.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.data.*
import com.example.tp.databinding.FragmentOptionBinding
import com.example.tp.interfaces.DataListener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class OptionFragment : DialogFragment(), DataListener {

    lateinit var binding:FragmentOptionBinding
    var sender:String=""
    var message:String=""
    var date:String=""
    var direction:String=""
    var goal=""
    private var encryption_type:String=""
    private var caesar_value:Int =0
    lateinit var ws : WebSocket
    private lateinit var listener:WebSocketListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentOptionBinding.inflate(inflater,container,false)

        initializeData()

        initializeWebSocket()

        if(encryption_type==Encryption.rotation.name)
            binding.edtValue.visibility=View.GONE

        binding.btnLeft.setOnClickListener {
            direction=Direction.left.name
            binding.btnLeft.setBackgroundResource(R.drawable.button_shape)
            binding.btnLeft.setTextColor(Color.WHITE)
            binding.btnRight.setBackgroundResource(R.drawable.button_shape_white)
            binding.btnRight.setTextColor(Color.BLACK)
        }

        binding.btnRight.setOnClickListener {
            direction=Direction.right.name
            binding.btnRight.setBackgroundResource(R.drawable.button_shape)
            binding.btnRight.setTextColor(Color.WHITE)
            binding.btnLeft.setBackgroundResource(R.drawable.button_shape_white)
            binding.btnLeft.setTextColor(Color.BLACK)
        }

        binding.btnSend.setOnClickListener {
            if (direction.isEmpty()){
                Toast.makeText(context,"choose a direction",Toast.LENGTH_SHORT).show()
            }else{
                if(encryption_type==Encryption.caesar.name && binding.edtValue.text.toString().isEmpty()){
                    Toast.makeText(context,"choose a caesar value",Toast.LENGTH_SHORT).show()
                }else{
                    if (goal=="websocket"){
                        if(encryption_type==Encryption.caesar.name)
                            caesar_value=binding.edtValue.text.toString().toInt()
                        val messageToSend=Message(sender, message,encryption_type,date,direction,caesar_value,null,null,null)
                        val gson = Gson()
                        val jsonString = gson.toJson(messageToSend)
                        ws.send(jsonString)
                    }else{
                        if (goal=="decryption"){
                            val encryptedMessageFragment= EncryptedMessageFragment()
                            encryptedMessageFragment.arguments=manageBundle(arguments?.getString("encryption_type","").toString())
                            encryptedMessageFragment.show(childFragmentManager,"EncryptedMessageFragment_fragment")
                        }
                    }
                }
            }
        }

        return binding.root
    }

    private fun initializeData(){
        sender= arguments?.getString("sender","").toString()
        message=arguments?.getString("message","").toString()
        date=arguments?.getString("date","").toString()
        encryption_type=arguments?.getString("encryption_type","").toString()
        goal=arguments?.getString("goal","").toString()
    }
    private fun initializeWebSocket(){
        val request : Request = Request
            .Builder()
            .url(URL.WEBSOCKET_URL)
            .build()
        val client= OkHttpClient()
        listener=WebSocketListener(this)
        ws =client.newWebSocket(request,listener)
    }

    override fun dataListener(message: MessageResponse) {
        activity?.runOnUiThread {
            dismiss()
        }
    }


    private fun manageBundle(encryptionType:String) :Bundle {
        val bundle =Bundle()
        bundle.putString("message",message)
        bundle.putString("sender",sender)
        bundle.putString("direction",direction)
        bundle.putString("encryption_type",encryptionType)
        if(encryptionType==Encryption.caesar.name)
            bundle.putInt("caesar_value",binding.edtValue.text.toString().toInt())
        return bundle
    }


}