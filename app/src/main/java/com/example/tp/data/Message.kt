package com.example.tp.data

import androidx.annotation.Nullable

data class Message(var sender:String,var message:String,var encryption_type:String,var date:String,
    var direction:String?,var caesar_value:Int?,var a:Int?,var b:Int?,var extra_char:String?)
