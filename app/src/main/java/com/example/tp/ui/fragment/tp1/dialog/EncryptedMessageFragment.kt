package com.example.tp.ui.fragment.tp1.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tp.data.*
import com.example.tp.data.encryption.AffineEncryption
import com.example.tp.data.encryption.CaesarEncryption
import com.example.tp.data.encryption.MirrorEncryption
import com.example.tp.data.encryption.RotationEncryption
import com.example.tp.databinding.FragmentEncryptedMessageBinding
import com.example.tp.ui.viewModel.MessageViewModel1


class EncryptedMessageFragment : DialogFragment() {

    lateinit var binding:FragmentEncryptedMessageBinding
    val messageViewModel1=MessageViewModel1()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentEncryptedMessageBinding.inflate(inflater,container,false)

        val sender =arguments?.getString("sender","user").toString()
        val message=arguments?.getString("message","There is a problem").toString()
        val direction=arguments?.getString("direction","").toString()
        val encryption_type=arguments?.getString("encryption_type","").toString()

        binding.txtSenderName.text=sender
        binding.txtMessage.text=message
        binding.txtAlgorithm.text="Algorithm is : $encryption_type"


        if (encryption_type==Encryption.caesar.name){
            val messageToEncrypt= CaesarEncryption(message,encryption_type,direction,
                arguments?.getInt("caesar_value",0)?.toInt()!!
            )
            messageViewModel1.getCaesarDecryption(messageToEncrypt)
        }else
            if (encryption_type==Encryption.rotation.name){
                val messageToEncrypt= RotationEncryption(message,encryption_type,direction)
                messageViewModel1.getRotationDecryption(messageToEncrypt)
            }else{
                if(encryption_type==Encryption.mirror.name){
                    val messageToEncrypt=
                        MirrorEncryption(message,arguments?.getString("extra_char","").toString())
                    messageViewModel1.getMirrorDecryption(messageToEncrypt)
                }else{
                    val a :Int=arguments?.getInt("a",0)!!.toInt()
                    val b :Int=arguments?.getInt("b",0)!!.toInt()
                    val messageToEncrypt= AffineEncryption(message,a,b)
                    messageViewModel1.getAffineDecryption(messageToEncrypt)
                }
            }


        MessageViewModel1.liveDecryption.observe(this){
            if (it!=null){
                binding.txtMessage.text=it
                binding.progressMessage.visibility=View.GONE
                binding.txtMessage.visibility=View.VISIBLE
            }
        }
        MessageViewModel1.liveAffineError.observe(this){
            if (it!=null){
                Toast.makeText(context,"Check your input",Toast.LENGTH_SHORT).show()
                MessageViewModel1.liveDecryption.value=null
                MessageViewModel1.liveAffineError.value=null
                dismiss()
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        binding.txtMessage.text=""
        binding.txtMessage.visibility=View.GONE
        binding.progressMessage.visibility=View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        MessageViewModel1.liveDecryption.value=null
    }


}