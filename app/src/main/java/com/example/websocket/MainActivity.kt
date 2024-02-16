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

    private var messageAdapter= MessageAdapter()
    private  val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        observers()
        listnerUi()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.messageRv.layoutManager = layoutManager
        binding.messageRv.adapter=messageAdapter
    }

    private fun observers() {
        viewModel.socketStatus.observe(this){
            binding.statusTV.text=if(it) "Connected" else "Disconnected"
        }

        viewModel.messages.observe(this){
            Log.e(TAG, "onCreate: ${if (it.first) "You: " else "Other: "} ${it.second}")
            Log.e(TAG, "onCreate IOMessage : ${IOMessage(it.first, it.second)}")
            messageAdapter.setData(IOMessage(it.first,it.second))

        }
    }

    private fun listnerUi() {
        binding.connectButton.setOnClickListener {
            viewModel.connectWebSocket()
        }

        binding.disconnectButton.setOnClickListener {
            viewModel.disconnectWebSocket()
        }

        binding.sendButton.setOnClickListener {
            viewModel.sendMessage(binding.messageET.text.toString())
            viewModel.addMessage(Pair(true, binding.messageET.text.toString()))
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}