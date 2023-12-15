package com.example.tp.ui.fragment.tp3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tp.R
import com.example.tp.databinding.FragmentWelcomeBinding
import com.example.tp.security.RSAEncryptor


class WelcomeFragment : Fragment() {

    lateinit var binding : FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
//        val key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4QVYiTa2JHQ8G8mwMVIwgrK2pWtnfmmmc2aHmhZxMltjP47YDxwe2PLU/dg0+ynSq6mG+RzY1nQogguk2mErlEr/x+u1d4lAkVpHZSWASuUgBMFjduCmPXhTIBHTVXVqJQ7Hg++i5X2e4j8hU8IVxKgO1vC/dbsXuBapwA9tritiqf8vAGskcaeEashSE1cD+NRxi8mmb3MEgIKhls6Kfj+VXx1nQTxrHSxgjUE2zzvM9YXAo6iNTd8GItXFuot1mQuq5oTWYcfRot/FMyJh+lBwfEE5WFMwqrM6heg8Ppo8l5w0M6TXe6CDJk4x1QlRCpkgMRtNYobIYMValth2lwIDAQAB"
//        val rsaEncryptor = RSAEncryptor(key)
//        val encryptedPassword = java.util.Base64.getEncoder().encodeToString(rsaEncryptor.encryptData("Nadir2023@"))
//        Log.d("TAG","aaaaaaaaaaaaaaaaaaa   "+encryptedPassword)
        binding.cardWelcome.setOnClickListener{
            findNavController().navigate(R.id.tp3EmailAuthenticationFragment)

        }

        return binding.root
    }

}