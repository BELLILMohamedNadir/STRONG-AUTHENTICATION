package com.example.tp.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tp.R
import com.example.tp.data.MessageResponse
import com.example.tp.interfaces.MessageListener


class RecyclerViewMessage (
    val  data:ArrayList<MessageResponse>, val name:String,val listener :MessageListener
        ) : RecyclerView.Adapter<RecyclerViewMessage.ViewHolderMessage>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMessage {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.message_layout,null,false)
        return ViewHolderMessage(view)
    }
    override fun onBindViewHolder(holder: ViewHolderMessage, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addMessage(message: MessageResponse){
        data.add(0,message)
        notifyDataSetChanged()
    }
    inner class ViewHolderMessage(itemView: View) : ViewHolder(itemView) {

        val containerReceiver :RelativeLayout=itemView.findViewById(R.id.container_message_receiver)
        val containerSender :RelativeLayout=itemView.findViewById(R.id.container_message_sender)
        val txtMessageReceiver :TextView=itemView.findViewById(R.id.txt_message_receiver)
        val txtMessageSender :TextView=itemView.findViewById(R.id.txt_message_sender)
        val txtDateReceiver :TextView=itemView.findViewById(R.id.txt_date_receiver)
        val txtDateSender :TextView=itemView.findViewById(R.id.txt_date_sender)

        fun onBind(message: MessageResponse){

            if (message.sender == name){
                containerReceiver.visibility=View.GONE
                containerSender.visibility=View.VISIBLE
                txtMessageSender.text=message.message
                txtDateSender.text=message.date
            }else{
                containerSender.visibility=View.GONE
                containerReceiver.visibility=View.VISIBLE
                txtMessageReceiver.text=message.message
                txtDateReceiver.text=message.date
            }
            containerReceiver.setOnClickListener{
                if (txtDateReceiver.isVisible)
                    txtDateReceiver.visibility=View.GONE
                else
                    txtDateReceiver.visibility=View.VISIBLE
            }
            containerReceiver.setOnLongClickListener {
                listener.onClick(data[adapterPosition])
                true
            }



            containerSender.setOnClickListener{
                if (txtDateSender.isVisible)
                    txtDateSender.visibility=View.GONE
                else
                    txtDateSender.visibility=View.VISIBLE
            }

        }
    }
}