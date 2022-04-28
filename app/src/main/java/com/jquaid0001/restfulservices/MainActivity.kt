package com.jquaid0001.restfulservices

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jquaid0001.restfulservices.databinding.ActivityMainBinding
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding!!.btnRESTful.setOnClickListener {
            // This is the URL that the service will call
            val url = "https://cs.okstate.edu/~bem/rectangleArea.php/3/4"

            // The HTTP request builder
            val request = Request.Builder().url(url).build()

            val client = OkHttpClient()

            // The code to execute the RESTful call
            // Do not do client.newcall(request).execute on the main thread!
            //      or the app will crash
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    print("Error retrieving JSON DATA")
                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string()

                    // Create the JSON parser object
                    val gson = Gson()
                    // Create a TypeToken object for the data class
                    val listType = object : TypeToken<RestData>() {}.type
                    // Convert the JSON string to a result
                    val result = gson.fromJson<RestData>(body, listType)

                    // On the UI thread, populate the text view with the result
                    runOnUiThread {
                        binding!!.tvRESTresult.text = "RESTful service returned " + result.area
                    }
                }
            })
        }
    }
}

// A data class for the RESTful response. Generally in a separate file
data class RestData(val area: Int)