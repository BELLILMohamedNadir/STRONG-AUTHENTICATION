package com.example.tp.ui.fragment.tp1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tp.`object`.URL
import com.example.tp.data.Password
import com.example.tp.databinding.FragmentPart2DefenseBinding
import com.example.tp.ui.viewModel.PasswordViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNet


class Part2DefenseFragment : Fragment()  {

    lateinit var binding:FragmentPart2DefenseBinding
    private val passwordViewModel =PasswordViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding=FragmentPart2DefenseBinding.inflate(inflater,container,false)

        var choice1=Regex("^[01]{3}$")
        var choice2=Regex("^[0-9]{5}$")
        var choice3=Regex("^[a-zA-Z0-9\\s\\S]{5}$")


        binding.btnSendPasswordDefense.setOnClickListener {
            val password=binding.edtPasswordDefense.text.toString()
            if (password.isEmpty()){
                Toast.makeText(context,"Add a password", Toast.LENGTH_SHORT).show()
            }else{
                SafetyNet.getClient(activity?.applicationContext!!)
                    .verifyWithRecaptcha(URL.RECAPTCHA)
                    .addOnSuccessListener {
                        if (it.tokenResult!!.isNotEmpty()){
                            binding.linearDataDefense.visibility=View.GONE
                            if (choice1.containsMatchIn(password) || choice2.containsMatchIn(password) || choice3.containsMatchIn(password) ){
                                binding.btnSendPasswordDefense.visibility=View.GONE
                                binding.progressSendPasswordDefense.visibility=View.VISIBLE
                                passwordViewModel.getPassword(Password(password))
                            }else{
                                Toast.makeText(context,"verify your password pattern", Toast.LENGTH_SHORT).show()
                            }
                        }else
                            Toast.makeText(context,"Retry",Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{
                        if (it is ApiException){
                            Log.d("recaptcha error","${it.message}")
                        }
                    }
            }
        }
        PasswordViewModel.livePassword.observe(this){
            if (it!=null){
                binding.progressSendPasswordDefense.visibility=View.GONE
                binding.btnSendPasswordDefense.visibility=View.VISIBLE
            }
        }
        PasswordViewModel.liveError.observe(this){
            if (it){
                binding.progressSendPasswordDefense.visibility=View.GONE
                binding.btnSendPasswordDefense.visibility=View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        PasswordViewModel.livePassword.value=null
    }


}
