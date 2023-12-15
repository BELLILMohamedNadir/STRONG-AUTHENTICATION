package com.example.tp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp.R
import com.example.tp.databinding.ActivityTp1Binding
import com.example.tp.databinding.ActivityTp2Binding

class Tp2Activity : AppCompatActivity() {

    lateinit var binding:ActivityTp2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTp2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}