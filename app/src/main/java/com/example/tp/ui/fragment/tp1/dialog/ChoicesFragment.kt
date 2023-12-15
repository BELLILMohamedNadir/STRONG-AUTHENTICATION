package com.example.tp.ui.fragment.tp1.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tp.`object`.URL
import com.example.tp.data.Encryption
import com.example.tp.data.MessageResponse
import com.example.tp.data.WebSocketListener
import com.example.tp.databinding.FragmentChoicesBinding
import com.example.tp.interfaces.DataListener
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class ChoicesFragment : DialogFragment(),DataListener {

    lateinit var binding:FragmentChoicesBinding
    lateinit var ws : WebSocket
    private lateinit var listener: WebSocketListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentChoicesBinding.inflate(inflater,container,false)

        val sender =arguments?.getString("sender","").toString()
        val message =arguments?.getString("message","").toString()
        val date =arguments?.getString("date","").toString()
        val goal=arguments?.getString("goal","").toString()


        initializeWebSocket()

        binding.btnMirror.setOnClickListener {
            val mirror = MirrorExtraCharFragment()
            mirror.arguments=manageBundle(Encryption.mirror.name,goal)
            mirror.show(childFragmentManager,"mirror_fragment")
        }

        binding.btnCaesar.setOnClickListener {
            val optionFragment= OptionFragment()
            optionFragment.arguments=manageBundle(Encryption.caesar.name,goal)
            optionFragment.show(childFragmentManager,"option_fragment")
        }

        binding.btnRotation.setOnClickListener {
            val optionFragment= OptionFragment()
            optionFragment.arguments=manageBundle(Encryption.rotation.name,goal)
            optionFragment.show(childFragmentManager,"option_fragment")
        }

        binding.btnAffine.setOnClickListener {
            val affineOptionFragment= AffineOptionFragment()
            affineOptionFragment.arguments=manageBundle(Encryption.affine.name,goal)
            affineOptionFragment.show(childFragmentManager,"affine_option_fragment")
        }

        return binding.root
    }
    fun isPalindrome(s: String): Boolean {
        val formattedString = s.toLowerCase()
        return formattedString == formattedString.reversed()
    }

    override fun dataListener(message: MessageResponse) {
        activity?.runOnUiThread {
            dismiss()
        }
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

    private fun manageBundle(encryptionType:String,goal:String) :Bundle {
        val bundle =Bundle()
        bundle.putString("message",arguments?.getString("message","").toString())
        bundle.putString("sender",arguments?.getString("sender","").toString())
        bundle.putString("encryption_type",encryptionType)
        bundle.putString("date",arguments?.getString("date","").toString())
        bundle.putString("goal",goal)
        return bundle
    }


}