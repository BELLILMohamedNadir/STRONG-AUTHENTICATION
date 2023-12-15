package com.example.tp.`object`

class URL {
    companion object{
        private const val IP_ADDRESS = "192.168.212.96"
        const val BASE_URL = "http://$IP_ADDRESS:8000/"
        const val WEBSOCKET_URL = "ws://${IP_ADDRESS}:8000/tp1/ws/chat/"
        const val RECAPTCHA = "API KEY FROM https://www.google.com/recaptcha/about/"
        const val ERROR = "ERROR 404"
        const val EMAIL = "email"
        const val PUBLIC_KEY = "publicKey"
        const val OK_200 = 200
        const val CODE_404 = 404
        const val CODE_423 = 423
        const val CODE_422 = 422
        const val CODE_401 = 401
        const val CODE_408 = 408
        const val CODE_500 = 500
    }
}