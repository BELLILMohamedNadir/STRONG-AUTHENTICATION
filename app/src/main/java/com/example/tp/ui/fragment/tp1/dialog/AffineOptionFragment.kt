package com.example.tp.ui.fragment.tp1.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tp.`object`.URL
import com.example.tp.data.Encryption
import com.example.tp.data.Message
import com.example.tp.data.MessageResponse
import com.example.tp.data.WebSocketListener
import com.example.tp.databinding.FragmentAffineOptionBinding
import com.example.tp.interfaces.DataListener
import com.example.tp.ui.viewModel.MessageViewModel1
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class AffineOptionFragment : DialogFragment(),DataListener {

    lateinit var binding:FragmentAffineOptionBinding
    lateinit var ws : WebSocket
    private lateinit var listener: WebSocketListener
    val messageViewModel1= MessageViewModel1()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentAffineOptionBinding.inflate(inflater,container,false)

        initializeWebSocket()

        val sender= arguments?.getString("sender","").toString()
        val message=arguments?.getString("message","").toString()
        val date=arguments?.getString("date","").toString()
        val goal=arguments?.getString("goal","").toString()

        binding.btnSendAffine.setOnClickListener {
            val a=binding.edtA.text.toString()
            val b=binding.edtB.text.toString()
            if (a.isEmpty())
                Toast.makeText(context,"choose variable a",Toast.LENGTH_SHORT).show()
            else{
                if (b.isEmpty())
                    Toast.makeText(context,"choose variable b",Toast.LENGTH_SHORT).show()
                else{
                    if (goal=="websocket"){
                        val messageToSend=
                            Message(sender, message,Encryption.affine.name,date,null,null,a.toInt(),b.toInt(),null)
                        val gson = Gson()
                        val jsonString = gson.toJson(messageToSend)
                        ws.send(jsonString)
                    }else{
                        if (goal=="decryption"){
                            val encryptedMessageFragment= EncryptedMessageFragment()
                            encryptedMessageFragment.arguments=manageBundle(arguments?.getString("encryption_type","").toString(),a.toInt(),b.toInt())
                            encryptedMessageFragment.show(childFragmentManager,"EncryptedMessageFragment_fragment")
                        }
                    }
                }
            }

        }

        return binding.root
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

    private fun manageBundle(encryptionType:String,a:Int,b:Int) :Bundle {
        val bundle =Bundle()
        bundle.putString("message",arguments?.getString("message","").toString())
        bundle.putString("sender",arguments?.getString("sender","").toString())
        bundle.putString("encryption_type",encryptionType)
        bundle.putInt("a",a)
        bundle.putInt("b",b)
        return bundle
    }


}