package com.example.tp.ui.fragment.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tp.R
import com.example.tp.databinding.FragmentTp3ContentBinding


class Tp3ContentFragment : Fragment() {

    lateinit var binding : FragmentTp3ContentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTp3ContentBinding.inflate(inflater, container, false)



        return binding.root
    }

    private fun loadPdfFromRawResource() {
//        // Get InputStream from raw resource
//        val inputStream: InputStream = resources.openRawResource(R.raw.security)
//
//        // Load PDF from InputStream
//        pdfView.fromStream(inputStream)
//            .load()
    }

}