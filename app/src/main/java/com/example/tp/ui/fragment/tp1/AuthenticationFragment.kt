package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentAuthenticationBinding


class AuthenticationFragment : Fragment() {

    lateinit var binding:FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentAuthenticationBinding.inflate(inflater,container,false)

        binding.btnAuthenticate.setOnClickListener {
            if (binding.edtUserName.text.toString().isEmpty()){
                binding.progressAuthenticate.visibility=View.GONE
                binding.btnAuthenticate.visibility=View.VISIBLE
                Toast.makeText(requireActivity(),"enter your User name",Toast.LENGTH_SHORT).show()
            }else{
                binding.btnAuthenticate.visibility=View.GONE
                binding.progressAuthenticate.visibility=View.VISIBLE
                val bundle:Bundle=Bundle()
                bundle.putString("user_name",binding.edtUserName.text.toString())
                findNavController().navigate(R.id.tp1ContainerFragment,bundle)
            }
        }

        return binding.root
    }

}