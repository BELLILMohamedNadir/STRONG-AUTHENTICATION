package com.example.tp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp.databinding.ActivityMainBinding
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Security.addProvider(BouncyCastleProvider())


        binding.btnTp1.setOnClickListener {
            startActivity(Intent(this, Tp1Activity::class.java))
        }
        binding.btnTp2.setOnClickListener {
            startActivity(Intent(this, Tp2Activity::class.java))
        }
        binding.btnTp3.setOnClickListener {
            startActivity(Intent(this, Tp3Activity::class.java))
        }
    }
}