package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentPart3ChoiceBinding


class Part3ChoiceFragment : Fragment() {

    lateinit var binding:FragmentPart3ChoiceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentPart3ChoiceBinding.inflate(inflater,container,false)

        binding.btnEncryption.setOnClickListener {
            findNavController().navigate(R.id.partie3Fragment)
        }

        binding.btnDecryption.setOnClickListener {
            findNavController().navigate(R.id.part3DecryptionFragment)
        }

        return binding.root
    }

}