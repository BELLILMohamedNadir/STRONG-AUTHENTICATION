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
import com.example.tp.data.user.UserEmail
import com.example.tp.databinding.FragmentTp3EmailAuthenticationBinding
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.util.STATUS


class Tp3EmailAuthenticationFragment : Fragment() {

    lateinit var binding:FragmentTp3EmailAuthenticationBinding
    var authenticationViewModel = AuthenticationViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentTp3EmailAuthenticationBinding.inflate(inflater,container,false)

        backPressed()

        binding.btnAuthenticateAuth.setOnClickListener {
            val email = binding.edtUserNameAuth.text.toString().trim()

            if (email.isNotEmpty()){
                if (Connection.isConnected(activity?.applicationContext)){
                    binding.btnAuthenticateAuth.visibility = View.GONE
                    binding.progressAuth.visibility = View.VISIBLE
                    AuthenticationViewModel.livePublicKey.value = null
                    requestPublicKey(email)
                }else{
                    Connection.makeToast(activity?.applicationContext)
                }
            }else{
                binding.progressAuth.visibility = View.GONE
                binding.btnAuthenticateAuth.visibility = View.VISIBLE
                Toast.makeText(context,"add your information ",Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }

    private fun requestPublicKey(email: String) {
        authenticationViewModel.requestPublicKey(UserEmail(email))

        AuthenticationViewModel.livePublicKey.observe(this){

            // is the response is not null
            if (it!=null){
                //is the user authenticated ?
                if (!it.equals("${URL.CODE_404}")){
                    if (!it.equals("${URL.CODE_423}")){
                        val bundle = Bundle()
                        bundle.putString(URL.EMAIL, email)
                        bundle.putString(URL.PUBLIC_KEY, it)
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.tp3PasswordAuthenticationFragment,bundle)
                    }else{
                        binding.progressAuth.visibility = View.GONE
                        binding.btnAuthenticateAuth.visibility = View.VISIBLE
                        STATUS.status(423, context,childFragmentManager)
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
        AuthenticationViewModel.livePublicKey.value = null
    }
}