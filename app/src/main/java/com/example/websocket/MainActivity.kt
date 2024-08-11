package com.example.websocket

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class MainActivity : AppCompatActivity() {
    private lateinit var webSocketListener: WebSocketListener
    private lateinit var mainViewModel: MainViewModel
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnConnect = findViewById<Button>(R.id.btn1)
        val btnDisconnect = findViewById<Button>(R.id.btn2)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val tvMassage = findViewById<TextView>(R.id.tvMassage)
        val edtMassage = findViewById<EditText>(R.id.edtMassage)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        webSocketListener = WebSocketListener(mainViewModel)
        mainViewModel.socketStatus.observe(this, Observer {
            tvMassage.text = if (it) "Connected" else "Disconnected"
        })
        var text = ""
        mainViewModel.massage.observe(this, Observer {
            text += "${if (it.first) "You :" else "Other:"} ${it.second}\n"
            tvMassage.text = text
        })
        btnConnect.setOnClickListener {
            webSocket = okHttpClient.newWebSocket(createRequest(), webSocketListener)
        }
        btnDisconnect.setOnClickListener {
            webSocket?.close(1000, "Cancelled Manually")
        }
        btnSend.setOnClickListener {
            if (edtMassage.text.toString().isNotEmpty()) {
                webSocket?.send(edtMassage.text.toString())
                mainViewModel.setMessage(Pair(false,edtMassage.text.toString()))
            } else {
                Toast.makeText(this, "Write Something Here...", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createRequest(): Request {
        val webSocketUrl =
            "wss://free.blr2.piesocket.com/v3/1?api_key=4tErSNncByIucr3kPP450OkYTS8iwdVFSZJyMQ3O&notify_self=1"
        return Request.Builder().url(webSocketUrl).build()

    }
}