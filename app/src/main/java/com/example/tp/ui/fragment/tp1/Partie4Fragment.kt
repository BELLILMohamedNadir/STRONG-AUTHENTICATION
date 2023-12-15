package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tp.R
import com.example.tp.databinding.FragmentPartie1Binding
import com.example.tp.databinding.FragmentPartie2Binding
import com.example.tp.databinding.FragmentPartie4Binding


class Partie4Fragment : Fragment() {

    private lateinit var binding: FragmentPartie4Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPartie4Binding.inflate(inflater,container,false)



        return binding.root
    }

}