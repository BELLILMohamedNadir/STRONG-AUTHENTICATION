package com.example.tp.ui.fragment.tp3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import android.opengl.Visibility
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.connection.Connection
import com.example.tp.data.AndroidID
import com.example.tp.databinding.FragmentTp3CheckInfoBinding
import com.example.tp.security.RSAEncryptor
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.ui.viewModel.ImageViewModel
import com.example.tp.util.STATUS
import java.util.Formatter

class Tp3CheckInfoFragment : Fragment() {

    lateinit var binding : FragmentTp3CheckInfoBinding
    val authenticationViewModel = AuthenticationViewModel()
    var email = ""
    var publicKey = ""
    var encryptedId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3CheckInfoBinding.inflate(inflater, container, false)
        backPressed()

        arguments?.let {
            email = it.getString(URL.EMAIL).toString()
            publicKey = it.getString(URL.PUBLIC_KEY).toString()
        }

        val rsaEncryptor = RSAEncryptor(publicKey)
        encryptedId = java.util.Base64.getEncoder().encodeToString(rsaEncryptor.encryptData(getAndroidId(requireContext())+""))

        if (Connection.isConnected(activity?.applicationContext))
            authenticationViewModel.checkInfo(AndroidID(email, encryptedId))
        else{
            Connection.makeToast(activity?.applicationContext)
            binding.progressSendEmail.visibility = View.GONE
            binding.btnRetrySend.visibility=View.VISIBLE
        }

        AuthenticationViewModel.liveCheckInfo.observe(this){

            if (it != null){
                if (it == URL.OK_200){
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.tp3ContentFragment)
                }else{
                    binding.progressSendEmail.visibility = View.GONE
                    binding.btnRetrySend.visibility=View.VISIBLE
                    STATUS.status(it, context, childFragmentManager)
                }
            }
        }

        binding.btnRetrySend.setOnClickListener {
            if (Connection.isConnected(activity?.applicationContext)){
                binding.btnRetrySend.visibility=View.GONE
                binding.progressSendEmail.visibility = View.VISIBLE
                AuthenticationViewModel.liveCheckInfo.value = null
                authenticationViewModel.checkInfo(AndroidID(email, encryptedId))
            }else
                Connection.makeToast(activity?.applicationContext)
        }

        return binding.root
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
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
        AuthenticationViewModel.liveCheckInfo.value = null
    }

}