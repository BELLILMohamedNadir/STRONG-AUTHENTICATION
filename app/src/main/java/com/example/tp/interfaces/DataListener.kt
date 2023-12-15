package com.example.tp.interfaces

import com.example.tp.data.MessageResponse

interface DataListener {
    fun dataListener(message: MessageResponse)
}