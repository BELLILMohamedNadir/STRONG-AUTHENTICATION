package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentTp1ContainerBinding


class Tp1ContainerFragment : Fragment() {

    lateinit var binding:FragmentTp1ContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentTp1ContainerBinding.inflate(inflater,container,false)
        if (arguments!=null)
            binding.txtUserName.text="Hello ,${arguments?.getString("user_name","")}"

        val navigation:NavController=findNavController()

        binding.btnPart2.setOnClickListener {
            val bundle= Bundle()
            bundle.putString("user_name",arguments?.getString("user_name",""))
            navigation.navigate(R.id.partie1Fragment,bundle)
        }
        binding.btnPart3.setOnClickListener {
            navigation.navigate(R.id.part2ChoiceFragment)
        }
        binding.btnPart4.setOnClickListener {
            navigation.navigate(R.id.part3ChoiceFragment)
        }


        return binding.root
    }

}