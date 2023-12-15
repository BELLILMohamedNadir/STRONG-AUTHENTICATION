package com.example.tp.data

data class MessageResponse(
    var sender: String?,
    var message: String?,
    var encryption_type: String?,
    var date: String?,
    var extra_char: String?,
    var direction: String?,
    var caesar_value: Int?,
    var a: Int?,
    var b: Int?,
    var error: String?
)
