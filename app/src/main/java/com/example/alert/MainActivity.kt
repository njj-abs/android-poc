package com.example.alert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val url = "https://jsonplaceholder.typicode.com/posts"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAlert = findViewById<Button>(R.id.button)
        val text = findViewById<TextView>(R.id.text)

        btnAlert.setOnClickListener {
            val artDialogBuilder = AlertDialog.Builder(this)

            artDialogBuilder.setMessage("Alert")
            artDialogBuilder.setCancelable(false)
            artDialogBuilder.setPositiveButton("yes") { _, _ ->
                finish()
            }
            val alertDialogBox = artDialogBuilder.create()
            alertDialogBox.show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val jsonResponse = fetchJson(url)
                // Use the parsed JSON data here (e.g., display in Toast, update UI)
                text.text = jsonResponse
            } catch (e: Exception) {
                // Handle errors gracefully, e.g., show user-friendly message
                text.text = e.toString()
            }
        }
        }

    private suspend fun fetchJson(urlString: String): String {
        return withContext(Dispatchers.IO) {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                response.toString()
            } else {
                throw IOException("Error: HTTP response code: $responseCode")
            }
        }
    }
}
