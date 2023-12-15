package com.example.tp.ui.fragment.tp3.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tp.R
import com.example.tp.databinding.FragmentRetryBinding


    class RetryFragment : DialogFragment() {

    lateinit var binding : FragmentRetryBinding
    var message = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRetryBinding.inflate(inflater, container, false)
        isCancelable = false

        arguments?.let {
            message = it.getString("message","").toString()
        }

        binding.txtMessage.text = message

        binding.btnRetry.setOnClickListener {
            activity?.finish()
        }

        return binding.root
    }

        companion object {
                fun newInstance(data: String): RetryFragment {
                val fragment = RetryFragment()
                val args = Bundle()
                args.putString("message", data)
                fragment.arguments = args
                return fragment
            }
        }
}