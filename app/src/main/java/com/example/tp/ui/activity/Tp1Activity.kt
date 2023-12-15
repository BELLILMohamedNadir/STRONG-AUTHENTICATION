package com.example.tp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tp.databinding.ActivityTp1Binding
//import com.example.tp.security.RSAEncryptor
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.*


class Tp1Activity : AppCompatActivity() {
    lateinit var binding:ActivityTp1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTp1Binding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}