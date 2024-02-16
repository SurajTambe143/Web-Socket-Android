package com.example.websocket

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.websocket.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var webSocketListener: WebSocketListner
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var messageAdapter= MessageAdapter()
    private  val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var text =""

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        webSocketListener = WebSocketListner(viewModel)
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.messageRv.layoutManager = layoutManager
        binding.messageRv.adapter=messageAdapter

        viewModel.socketStatus.observe(this){
            binding.statusTV.text=if(it) "Connected" else "Disconnected"
        }

        viewModel.messages.observe(this){
            text += "${if (it.first) "You: " else "Other: "} ${it.second}\n"
            Log.e(TAG, "onCreate: ${if (it.first) "You: " else "Other: "} ${it.second}")
            Log.e(TAG, "onCreate IOMessage : ${IOMessage(it.first, it.second)}")
            messageAdapter.setData(IOMessage(it.first,it.second))

        }

        binding.connectButton.setOnClickListener {
            webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        }

        binding.disconnectButton.setOnClickListener {
            webSocket?.close(1000, "Canceled manually.")
        }

        binding.sendButton.setOnClickListener {
            webSocket?.send(binding.messageET.text.toString())
            viewModel.addMessage(Pair(true, binding.messageET.text.toString()))
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun createRequest(): Request {
        val websocketURL = "wss://${Constants.CLUSTER_ID}.piesocket.com/v3/1?api_key=${Constants.API_KEY}"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        okHttpClient.dispatcher.executorService.shutdown()
    }
}