package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tp.data.Password
import com.example.tp.databinding.FragmentPartie2Binding
import com.example.tp.ui.viewModel.PasswordViewModel


class Partie2Fragment : Fragment() {
    private lateinit var binding: FragmentPartie2Binding
    private val passwordViewModel =PasswordViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPartie2Binding.inflate(inflater,container,false)

        var choice1=Regex("^[01]{3}$")
        var choice2=Regex("^[0-9]{5}$")
        var choice3=Regex("^[a-zA-Z0-9\\s\\S]{5}$")

        binding.btnSendPassword.setOnClickListener {
            val password=binding.edtPassword.text.toString()
            if (password.isEmpty()){
                Toast.makeText(context,"Add a password",Toast.LENGTH_SHORT).show()
            }else{
                binding.linearData.visibility=View.GONE
                if (choice1.containsMatchIn(password) || choice2.containsMatchIn(password) || choice3.containsMatchIn(password) ){
                    binding.btnSendPassword.visibility=View.GONE
                    binding.progressSendPassword.visibility=View.VISIBLE
                    passwordViewModel.getPassword(Password(password))
                }else{
                    Toast.makeText(context,"verify your password pattern",Toast.LENGTH_SHORT).show()
                }
            }
        }
        PasswordViewModel.livePassword.observe(this){
            if (it!=null){
                binding.progressSendPassword.visibility=View.GONE
                binding.btnSendPassword.visibility=View.VISIBLE
                binding.linearData.visibility=View.VISIBLE
                binding.txtFound.text="Found :${it?.found}"
                binding.txtIteration.text="Iterations :${it?.iterations}"
                binding.txtEstimation.text="Estimated Time :${it?.estimated_time} s"
            }
        }
        PasswordViewModel.liveError.observe(this){
            if (it){
                binding.progressSendPassword.visibility=View.GONE
                binding.btnSendPassword.visibility=View.VISIBLE
            }
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        PasswordViewModel.livePassword.value=null
    }
}