package com.example.tp.ui.fragment.tp3


import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tp.databinding.FragmentTp3EmailConfiramtionBinding
import com.example.tp.R
import com.example.tp.`object`.URL
import com.example.tp.connection.Connection
import com.example.tp.data.request.TwoFactorRequest
import com.example.tp.ui.fragment.tp3.dialog.ConfirmFragment
import com.example.tp.ui.viewModel.AuthenticationViewModel
import com.example.tp.util.STATUS

class Tp3EmailConfiramtionFragment : Fragment() {

    lateinit var binding:FragmentTp3EmailConfiramtionBinding
    var authenticationViewModel = AuthenticationViewModel()
    var attemptsCounter = 0
    private var countDownTimer: CountDownTimer? = null
    private var email = ""
    private var edt1 = ""
    private var edt2 = ""
    private var edt3 = ""
    private var edt4 = ""
    private var edt5 = ""
    var publicKey = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentTp3EmailConfiramtionBinding.inflate(inflater,container,false)

        backPressed()

        arguments?.let {
            email = it.getString(URL.EMAIL).toString()
            publicKey = it.getString(URL.PUBLIC_KEY).toString()
        }


        setEditTextListeners(binding.edt1,binding.edt2,binding.edt1)
        setEditTextListeners(binding.edt2,binding.edt3,binding.edt1)
        setEditTextListeners(binding.edt3,binding.edt4,binding.edt2)
        setEditTextListeners(binding.edt4,binding.edt5,binding.edt3)
        setEditTextListeners(binding.edt5,binding.edt5,binding.edt4)

        startCountdown(binding.txtCounter)

        binding.btnAuth.setOnClickListener {

            getDataFromEditText()

            if (email.isNotEmpty() && edt1.isNotEmpty() && edt2.isNotEmpty() && edt3.isNotEmpty()
                && edt4.isNotEmpty()  && edt5.isNotEmpty() ){
                if (Connection.isConnected(activity?.applicationContext)){
                    binding.btnAuth.visibility = View.GONE
                    binding.progressAuth.visibility = View.VISIBLE
                    binding.txtNotify.setTextColor(Color.WHITE)
                    binding.txtNotify.text = resources.getText(R.string.provide_code)
                    AuthenticationViewModel.liveResponseTwoFactorAuth.value = null
                    verifyCode()
                }else{
                    Connection.makeToast(activity?.applicationContext)
                }
            }else{
                Toast.makeText(context, "Fill the void", Toast.LENGTH_SHORT).show()
                binding.progressAuth.visibility = View.GONE
                binding.btnAuth.visibility = View.VISIBLE
            }
        }

        binding.txtTry.setOnClickListener{
            val bundle = Bundle()
            bundle.putString(URL.EMAIL, email)
            findNavController().popBackStack()
            findNavController().navigate(R.id.tp3SendEmailFragment, bundle)
        }

        return binding.root
    }

    private fun verifyCode() {
        val code = edt1 + edt2 + edt3 + edt4 + edt5 + "".trim()

        authenticationViewModel.responseTwoFactorAuth(TwoFactorRequest(email, code))
        AuthenticationViewModel.liveResponseTwoFactorAuth.observe(this){

            if (it != null){

                if (it == URL.OK_200){
                    val bundle = Bundle()
                    bundle.putString(URL.EMAIL, email)
                    bundle.putString(URL.PUBLIC_KEY, publicKey)
                    findNavController().popBackStack()
                    findNavController().navigate(R.id.tp3FaceRecognitionFragment, bundle)
                }else {
                    binding.progressAuth.visibility = View.GONE
                    binding.btnAuth.visibility = View.VISIBLE
                    STATUS.status(it,context, childFragmentManager)
                    binding.txtNotify.setTextColor(Color.RED)
                    binding.txtNotify.text = resources.getText(R.string.wrong_code)
                    Toast.makeText(context, "check your code", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun getDataFromEditText() {
        edt1 = binding.edt1.text.toString()
        edt2 = binding.edt2.text.toString()
        edt3 = binding.edt3.text.toString()
        edt4 = binding.edt4.text.toString()
        edt5 = binding.edt5.text.toString()
    }


    private fun enableEditText(b: Boolean) {
        binding.edt1.isEnabled = b
        binding.edt2.isEnabled = b
        binding.edt3.isEnabled = b
        binding.edt4.isEnabled = b
        binding.edt5.isEnabled = b

        binding.btnAuth.isEnabled = b
    }

    private fun setEditTextListeners(currentEdt:EditText , nextEdt:EditText ,beforeEdt:EditText) {
        currentEdt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val digit = "$p0"
                if (digit.isNotEmpty()){
                    nextEdt.requestFocus()
                }else{
                    beforeEdt.requestFocus()
                }
            }

        })
    }

    private fun startCountdown(countdownText : TextView) {
        attemptsCounter ++
        countDownTimer = object : CountDownTimer(60 * 1000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished / 1000 <= 10)
                    countdownText.setTextColor(Color.RED)

                countdownText.text = " ${millisUntilFinished / 1000} s"
            }

            override fun onFinish() {
                enableEditText(false)
                binding.txtTry.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun backPressed(){
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                val confirmFragment = ConfirmFragment()
                confirmFragment.show(childFragmentManager, "confirm_fragment")
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onPause() {
        super.onPause()
        AuthenticationViewModel.liveResponseTwoFactorAuth.value = null
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }


}