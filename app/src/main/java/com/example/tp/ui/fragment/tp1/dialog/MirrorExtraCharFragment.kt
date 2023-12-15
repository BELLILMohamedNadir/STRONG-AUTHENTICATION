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
import com.example.tp.databinding.FragmentMirrorExtraCharBinding
import com.example.tp.interfaces.DataListener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MirrorExtraCharFragment : DialogFragment(),DataListener {

    lateinit var binding:FragmentMirrorExtraCharBinding
    var sender:String=""
    var message:String=""
    var date:String=""
    var goal=""
    private var encryption_type:String=""
    lateinit var ws : WebSocket
    private lateinit var listener: WebSocketListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentMirrorExtraCharBinding.inflate(inflater,container,false)

        initializeData()

        initializeWebSocket()

        binding.btnSendMirror.setOnClickListener {
            val extra=binding.edtExtraChar.text.toString()
            if (extra.isEmpty()){
                Toast.makeText(context,"Add the extra character",Toast.LENGTH_SHORT).show()
            }else{

                if (goal == "websocket") {
                    if (subShouldBeChanged(message,extra)){
                        Toast.makeText(context,"Choose an other extra character",Toast.LENGTH_SHORT).show()
                    }else{
                        val messageToSend = Message(
                            sender,
                            message,
                            Encryption.mirror.name,
                            date,
                            null,
                            null,
                            null,
                            null,
                            extra
                        )
                        val gson = Gson()
                        val jsonString = gson.toJson(messageToSend)
                        ws.send(jsonString)
                    }
                } else {
                    if (goal == "decryption") {
                        val encryptedMessageFragment = EncryptedMessageFragment()
                        val bundle = Bundle()
                        bundle.putString(
                            "message",
                            arguments?.getString("message", "").toString()
                        )
                        bundle.putString(
                            "sender",
                            arguments?.getString("sender", "").toString()
                        )
                        bundle.putString("extra_char", extra)
                        bundle.putString("encryption_type", Encryption.mirror.name)
                        encryptedMessageFragment.arguments = bundle
                        encryptedMessageFragment.show(
                            childFragmentManager,
                            "EncryptedMessageFragment_fragment"
                        )
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
    override fun dataListener(message: MessageResponse) {
        activity?.runOnUiThread {
            dismiss()
        }
    }
    fun subShouldBeChanged(s: String, sub: String): Boolean {
        if(sub.isNullOrEmpty())
            return true
        val reversed = s.reversed()
        return reversed.contains(sub)
    }

    private fun initializeWebSocket(){
        val request : Request = Request
            .Builder()
            .url(URL.WEBSOCKET_URL)
            .build()
        val client= OkHttpClient()
        listener=com.example.tp.data.WebSocketListener(this)
        ws =client.newWebSocket(request,listener)
    }

}