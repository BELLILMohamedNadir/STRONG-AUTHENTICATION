package com.example.tp.interfaces

import com.example.tp.data.Message
import com.example.tp.data.MessageResponse

interface MessageListener {
    fun onClick(message : MessageResponse)
}