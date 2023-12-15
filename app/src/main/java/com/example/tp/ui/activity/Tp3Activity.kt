package com.example.tp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp.R
import com.example.tp.databinding.ActivityTp2Binding
import com.example.tp.databinding.ActivityTp3Binding

class Tp3Activity : AppCompatActivity() {
    lateinit var binding:ActivityTp3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTp3Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}