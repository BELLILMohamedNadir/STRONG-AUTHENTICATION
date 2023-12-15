package com.example.tp.ui.fragment.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentTp3FirstFactorAcceptedBinding


class Tp3FirstFactorAcceptedFragment : Fragment() {


    lateinit var binding : FragmentTp3FirstFactorAcceptedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3FirstFactorAcceptedBinding.inflate(inflater, container, false)

        binding.cardAccepted.setOnClickListener{
            findNavController().popBackStack()
            findNavController().navigate(R.id.tp3EmailConfiramtionFragment)
        }

        return binding.root
    }

}