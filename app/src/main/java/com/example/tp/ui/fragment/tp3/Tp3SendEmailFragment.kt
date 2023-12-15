package com.example.tp.ui.fragment.tp3

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
import com.example.tp.databinding.FragmentTp3SendEmailBinding
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.util.STATUS


class Tp3SendEmailFragment : Fragment() {

    lateinit var binding : FragmentTp3SendEmailBinding

    var authenticationViewModel = AuthenticationViewModel()
    var email = ""
    var publicKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3SendEmailBinding.inflate(inflater, container, false)

        backPressed()

        arguments?.let {
            email = it.getString(URL.EMAIL).toString()
            publicKey = it.getString(URL.PUBLIC_KEY).toString()
        }

        binding.txtState.text = resources.getText(R.string.processing_request)

        if (Connection.isConnected(activity?.applicationContext)){
            AuthenticationViewModel.liveRequestTwoFactorAuth.value = null
            authenticationViewModel.requestTwoFactorAuth(UserEmail(email))
        }else{
            Connection.makeToast(activity?.applicationContext)
            binding.progressSendEmail.visibility = View.GONE
            binding.btnRetrySend.visibility =View.VISIBLE
            binding.txtState.text = resources.getText(R.string.processing_failed)
        }

        AuthenticationViewModel.liveRequestTwoFactorAuth.observe(this){
            if (it != null){
                if (it == URL.OK_200){
                    val bundle = Bundle()
                    bundle.putString(URL.EMAIL, email)
                    bundle.putString(URL.PUBLIC_KEY, publicKey)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.tp3EmailConfiramtionFragment,bundle)
                }else{
                    binding.progressSendEmail.visibility = View.GONE
                    binding.btnRetrySend.visibility =View.VISIBLE
                    STATUS.status(it,context, childFragmentManager)
                    binding.txtState.text = resources.getText(R.string.processing_failed)
                }
            }
        }

        binding.btnRetrySend.setOnClickListener {
            if (Connection.isConnected(activity?.applicationContext)){
                binding.btnRetrySend.visibility = View.GONE
                binding.linearSend.visibility = View.VISIBLE
                binding.txtState.text = resources.getText(R.string.processing_request)
                AuthenticationViewModel.liveRequestTwoFactorAuth.value = null
                authenticationViewModel.requestTwoFactorAuth(UserEmail(email))
            }else{
                Connection.makeToast(activity?.applicationContext)
            }
        }

        return binding.root
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
        AuthenticationViewModel.liveRequestTwoFactorAuth.value = null
    }
}