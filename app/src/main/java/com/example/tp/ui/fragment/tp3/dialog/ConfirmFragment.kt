package com.example.tp.ui.fragment.tp3.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentConfirmBinding


class ConfirmFragment : DialogFragment() {

    lateinit var binding : FragmentConfirmBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmBinding.inflate(inflater, container, false)

        isCancelable = false

        binding.btnConfirm.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}