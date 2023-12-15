package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp.interfaces.DataListener
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.data.Encryption
import com.example.tp.data.MessageResponse
import com.example.tp.data.WebSocketListener
import com.example.tp.databinding.FragmentPartie1Binding
import com.example.tp.interfaces.MessageListener
import com.example.tp.recyclerView.RecyclerViewMessage
import com.example.tp.ui.fragment.tp1.dialog.ChoicesFragment
//import com.example.tp.ui.viewModel.MessageViewModel
//import com.example.tp.ui.viewModel.MessageViewModel1
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Partie1Fragment : Fragment(), DataListener,MessageListener {

    private lateinit var binding:FragmentPartie1Binding
    private lateinit var data:ArrayList<MessageResponse>
    private var sender:String=""
    private var SentMessage=""
    private var date=""
    lateinit var ws : WebSocket
    lateinit var listener:WebSocketListener
    lateinit var recyclerViewMessage:RecyclerViewMessage
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentPartie1Binding.inflate(inflater,container,false)

        sender= arguments?.getString("user_name","").toString()
        binding.txtUserName.text=sender

        initializeWebSocket()

        data= ArrayList()
        recyclerViewMessage= RecyclerViewMessage(data,sender,this)
        binding.rvMessage.adapter=recyclerViewMessage
        binding.rvMessage.layoutManager=LinearLayoutManager(context,RecyclerView.VERTICAL,true)
        binding.rvMessage.setHasFixedSize(true)


        binding.imgSend.setOnClickListener {

            if (binding.edtMessage.text.toString().isEmpty())
                Toast.makeText(context,"add a message",Toast.LENGTH_SHORT).show()
            else{
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
                date = LocalDateTime.now().format(formatter)
                SentMessage=binding.edtMessage.text.toString()
                val choicesFragment= ChoicesFragment()
                val bundle=Bundle()
                bundle.putString("message",SentMessage)
                bundle.putString("sender",sender)
                bundle.putString("date",date)
                bundle.putString("goal","websocket")
                choicesFragment.arguments=bundle
                choicesFragment.show(childFragmentManager,"choices_fragment")

            }
        }


        binding.imgBack.setOnClickListener {
            findNavController().popBackStack(R.id.tp1ContainerFragment,false)
        }

        return binding.root
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

    override fun onDestroy() {
        super.onDestroy()
        ws.cancel()
    }

    override fun dataListener(message: MessageResponse) {
        activity?.runOnUiThread {
            if (message.error==null || message.error =="null"){
                if (message.sender!=sender){
                    recyclerViewMessage.addMessage(message)
                }else{
                    recyclerViewMessage.addMessage(MessageResponse(sender,SentMessage,null,date,null,null,null,null,null,null))
                }
                binding.edtMessage.text.clear()
            }else{
                if(sender==message.sender){
                    Toast.makeText(context,"check your input",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onClick(message: MessageResponse) {
        val choicesFragment= ChoicesFragment()
        val bundle=Bundle()
        bundle.putString("message",message.message)
        bundle.putString("sender",message.sender)
        bundle.putString("direction",message.direction)
        bundle.putString("encryption_type",message.encryption_type)
        bundle.putString("goal","decryption")
        if (message.encryption_type==Encryption.caesar.name)
            bundle.putInt("caesar_value", message.caesar_value!!.toInt())
        if (message.encryption_type==Encryption.mirror.name)
            bundle.putString("extra_char", message.extra_char)
        if (message.encryption_type==Encryption.affine.name)
            bundle.putInt("a", message.a!!.toInt())
        if (message.encryption_type==Encryption.affine.name)
            bundle.putInt("b", message.b!!.toInt())

        choicesFragment.arguments=bundle
        choicesFragment.show(childFragmentManager,"choices_fragment")
    }


}