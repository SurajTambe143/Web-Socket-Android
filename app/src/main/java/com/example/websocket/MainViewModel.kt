package com.example.websocket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    private val TAG = "MainViewModel"

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

}