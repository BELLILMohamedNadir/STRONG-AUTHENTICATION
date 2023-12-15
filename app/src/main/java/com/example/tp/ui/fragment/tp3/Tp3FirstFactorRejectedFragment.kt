package com.example.tp.ui.fragment.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentTp3FirstFactorRejectedBinding

class Tp3FirstFactorRejectedFragment : Fragment() {


    lateinit var binding : FragmentTp3FirstFactorRejectedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3FirstFactorRejectedBinding.inflate(inflater, container, false)

        binding.cardRejected.setOnClickListener{
            findNavController().popBackStack()
            findNavController().navigate(R.id.tp3FaceRecognitionFragment)
        }

        return binding.root
    }

}