package com.example.apis

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apis.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var myAdapter: MyAdapter
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView with a LinearLayoutManager
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        // Initialize adapter with an empty list
        myAdapter = MyAdapter(this, emptyList())
        binding.recyclerview.adapter = myAdapter

        // Set up SearchView listener to filter the list based on the query
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter.filter(newText)
                return false
            }
        })


        // Fetch data from the API
        getData()
    }

    private fun getData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(apiInterface::class.java)

        val retrofitData = retrofitBuilder.getdata()

        retrofitData.enqueue(object : Callback<List<mydataItem>?> {
            override fun onResponse(
                call: Call<List<mydataItem>?>,
                response: Response<List<mydataItem>?>
            ) {
                val responseBody = response.body()
                if (responseBody != null) {
                    myAdapter.updateData(responseBody)
                }
            }

            override fun onFailure(call: Call<List<mydataItem>?>, t: Throwable) {
                Log.d("MainActivity", "onFailure: ${t.message}")
            }
        })
    }
}

