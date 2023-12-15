package com.example.tp.data

import android.util.Log
import com.example.tp.interfaces.DataListener
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.Response
import okhttp3.WebSocket

class WebSocketListener(var messageListener: DataListener) : okhttp3.WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)

        Log.e("eeeeeeeeeeeeeeeeeeeeeeeeeeee","open")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val gson = GsonBuilder()
            .create()
        val message: JsonObject = gson.fromJson(
            text,
            JsonObject::class.java
        )
        outPut("${customizeData(message)}")
        messageListener.dataListener(customizeData(message))
    }

    private fun customizeData(json: JsonObject): MessageResponse {
        val sender = json.get("sender")?.toString()?.removeSurrounding("\"")
        val message = json.get("message")?.toString()?.removeSurrounding("\"")?.replace("\\\"", "\"")
        val encryption_type = json.get("encryption_type")?.toString()?.removeSurrounding("\"")
        val date = json.get("date")?.toString()?.removeSurrounding("\"")?.substring(0, 19)
        val extra_char: String? = json.get("extra_char")?.toString()?.removeSurrounding("\"")
        val direction: String? = json.get("direction")?.toString()?.removeSurrounding("\"")
        val caesar_value: Int? = json.get("caesar_value")?.toString()?.toIntOrNull()
        val a: Int? = json.get("a")?.toString()?.toIntOrNull()
        val b: Int? = json.get("b")?.toString()?.toIntOrNull()
        val error: String? = json.get("error")?.toString()?.removeSurrounding("\"")
        return MessageResponse(sender, message, encryption_type, date, extra_char, direction, caesar_value, a, b, error)
    }



    fun outPut(text:String){
        Log.d("eeeeeeeeeeeeeeeeeeeeeeeeeeee",text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        webSocket.close(NORMAL_CLOSURE_STATUS,null)
        outPut("closing :$code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        outPut("failed : ${t.message}")
    }
    companion object{
        private const val NORMAL_CLOSURE_STATUS=1000
    }
}