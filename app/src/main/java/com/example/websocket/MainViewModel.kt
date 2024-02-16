package com.example.websocket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainViewModel:ViewModel() {

    private val TAG = "MainViewModel"
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    private val _socketStatus= MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages =MutableLiveData<Pair<Boolean,String>>()
    val messages: LiveData<Pair<Boolean, String>> = _messages

    fun addMessage(message:Pair<Boolean,String>)= viewModelScope.launch(Dispatchers.IO) {
        if (_socketStatus.value==true){
            _messages.postValue(message)
            Log.e(TAG, "addMessage: is called $message", )
        }
    }

    fun setStatus(status :Boolean) =viewModelScope.launch(Dispatchers.IO) {
        _socketStatus.postValue(status)
    }

    fun connectWebSocket(){
        webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
    }

    fun disconnectWebSocket(){
        webSocket?.close(1000, "Canceled manually.")
    }

    fun sendMessage(message:String){
        webSocket?.send(message)
    }

    private fun createRequest(): Request {
        val websocketURL = "wss://${Constants.CLUSTER_ID}.piesocket.com/v3/1?api_key=${Constants.API_KEY}"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    override fun onCleared() {
        okHttpClient.dispatcher.executorService.shutdown()
    }

    private val webSocketListener = object: WebSocketListener(){
        private val TAG = "WebSocketListner"

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d(TAG, "onOpen:")
            setStatus(true)
            webSocket.send("Android Device Connected")
            super.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "onMessage: $text")
            addMessage(Pair(false, text))
            super.onMessage(webSocket, text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClosing: $code $reason")
            super.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d(TAG, "onClosed: $code $reason")
            setStatus(false)
            super.onClosed(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d(TAG, "onFailure: ${t.message} $response")
            super.onFailure(webSocket, t, response)
        }
    }

}