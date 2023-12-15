package com.example.tp.ui.fragment.tp3

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.connection.Connection
import com.example.tp.data.User
import com.example.tp.data.user.UserEmail
import com.example.tp.databinding.FragmentTp3PasswordAuthenticationBinding
import com.example.tp.security.RSAEncryptor
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.util.STATUS
import org.bouncycastle.jcajce.provider.asymmetric.RSA

class Tp3PasswordAuthenticationFragment : Fragment() {

    lateinit var binding: FragmentTp3PasswordAuthenticationBinding
    var authenticationViewModel = AuthenticationViewModel()
    private var email = ""
    private var publicKey= ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3PasswordAuthenticationBinding.inflate(inflater,container,false)

        backPressed()

        arguments?.let {
            email = it.getString(URL.EMAIL).toString()
            publicKey = it.getString(URL.PUBLIC_KEY).toString()
        }

        binding.btnAuthenticateAuth.setOnClickListener {
            val password = binding.edtPasswordAuth.text.toString().trim()
            binding.txtNotify.setTextColor(Color.WHITE)
            if (password.isNotEmpty() && email.isNotEmpty() && publicKey.isNotEmpty()){
                if (Connection.isConnected(activity?.applicationContext)){
                    binding.btnAuthenticateAuth.visibility = View.GONE
                    binding.progressAuth.visibility = View.VISIBLE
                    AuthenticationViewModel.liveAuthenticateUser.value = null
                    authenticateUser(password)
                }else
                    Connection.makeToast(activity?.applicationContext)
            }else{
                binding.progressAuth.visibility = View.GONE
                binding.btnAuthenticateAuth.visibility = View.VISIBLE
                Toast.makeText(context,"check your information ", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun authenticateUser(password: String) {
        val rsaEncryptor = RSAEncryptor(publicKey)
        val encryptedPassword = java.util.Base64.getEncoder().encodeToString(rsaEncryptor.encryptData(password))

        authenticationViewModel.authenticateUser(User(email, encryptedPassword))

        AuthenticationViewModel.liveAuthenticateUser.observe(this){

            if (it != null){
                if (it != URL.CODE_404){
                    if (it == URL.OK_200){
                        val bundle = Bundle()
                        bundle.putString(URL.EMAIL, email)
                        bundle.putString(URL.PUBLIC_KEY, publicKey)
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.tp3SendEmailFragment,bundle)
                    }else{
                        binding.progressAuth.visibility = View.GONE
                        binding.btnAuthenticateAuth.visibility = View.VISIBLE
                        Toast.makeText(context, "Verify your password", Toast.LENGTH_SHORT).show()
                        binding.txtNotify.setTextColor(Color.RED)
                    }
                }else{
                    binding.progressAuth.visibility = View.GONE
                    binding.btnAuthenticateAuth.visibility = View.VISIBLE
                    STATUS.status(404, context,childFragmentManager)
                }
            }
        }
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
        AuthenticationViewModel.liveAuthenticateUser.value = null
    }


}