package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentPart2ChoiceBinding


class Part2ChoiceFragment : Fragment() {

    lateinit var binding:FragmentPart2ChoiceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentPart2ChoiceBinding.inflate(inflater,container,false)

        binding.btnAttack.setOnClickListener {
            findNavController().navigate(R.id.partie2Fragment)
        }
        binding.btnDefense.setOnClickListener {
            findNavController().navigate(R.id.part2DefenseFragment)
        }

        return binding.root
    }

}