import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.apis.apiInterface
import com.example.apis.databinding.ActivityMainBinding
import com.example.apis.mydataItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyAdapter
    private val baseUrl = "https://jsonplaceholder.typicode.com/"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        fetchData()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this, emptyList())
        binding.recyclerview.adapter = myAdapter
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myAdapter.filter(newText)
                return true
            }
        })
    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(apiInterface::class.java)
        val call = apiService.getdata()

        call.enqueue(object : Callback<List<mydataItem>> {
            override fun onResponse(call: Call<List<mydataItem>>, response: Response<List<mydataItem>>) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        myAdapter.updateData(data)
                    } ?: run {
                        Log.e("MainActivity", "Response body is null")
                    }
                } else {
                    Log.e("MainActivity", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<mydataItem>>, t: Throwable) {
                Log.e("MainActivity", "Error fetching data: ${t.message}")
                // Consider showing a Toast or other user feedback here
            }
        })
    }
}
