package com.example.websocket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _seocketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _seocketStatus
    private val _message = MutableLiveData<Pair<Boolean, String>>()
    val massage: LiveData<Pair<Boolean, String>> = _message
    fun setStatus(status: Boolean) = GlobalScope.launch(Dispatchers.Main) {
        _seocketStatus.value = status
    }

    fun setMessage(message: Pair<Boolean, String>) = GlobalScope.launch(Dispatchers.Main) {
        if (_seocketStatus.value == true) {
            _message.value = message
        }
    }
}