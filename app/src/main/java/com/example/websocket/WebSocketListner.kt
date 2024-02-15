package com.example.websocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListner(private val viewModel: MainViewModel) : WebSocketListener() {
    private val TAG = "WebSocketListner"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "onOpen:")
        viewModel.setStatus(true)
        webSocket.send("Android Device Connected")
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d(TAG, "onMessage: $text")
        viewModel.addMessage(Pair(false, text))
        super.onMessage(webSocket, text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "onClosing: $code $reason")
        super.onClosing(webSocket, code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d(TAG, "onClosed: $code $reason")
        viewModel.setStatus(false)
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }

}