package com.example.alert

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

fun main() {
    val url = "https://jsonplaceholder.typicode.com/posts"
    val jsonResponse = fetchJson(url)

    // Parse JSON response
    val jsonArray = JSONArray(jsonResponse)
    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val title = jsonObject.getString("title")
        println("Title: $title")
    }
}

fun fetchJson(urlString: String): String {
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
        return response.toString()
    } else {
        throw Exception("Error: HTTP response code: $responseCode")
    }
}
