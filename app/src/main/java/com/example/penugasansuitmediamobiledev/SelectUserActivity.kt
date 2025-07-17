package com.example.penugasansuitmediamobiledev

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.penugasansuitmediamobiledev.api.ApiClient
import com.example.penugasansuitmediamobiledev.databinding.ActivitySelectUserBinding
import com.example.penugasansuitmediamobiledev.model.User
import com.example.penugasansuitmediamobiledev.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectUserBinding
    private lateinit var adapter: UserAdapter

    private var currentPage = 1
    private val perPage = 10
    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter { user ->
            val resultIntent = Intent()
            val selected_name = "${user.first_name} ${user.last_name}"
            resultIntent.putExtra("EXTRA_SELECTED_USER", selected_name)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        fetchUsers()

        binding.swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            isLastPage = false
            adapter.clear()
            fetchUsers()
        }

        binding.rvUser.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && !isLastPage && lastVisibleItem >= totalItemCount - 2) {
                    currentPage++
                    fetchUsers()
                }
            }
        })


        binding.activityToolbar.toolbarTitle.text = "Select User"
        binding.activityToolbar.btnBack.setOnClickListener { finish() }
    }

    private fun fetchUsers() {
        isLoading = true
        binding.swipeRefreshLayout.isRefreshing = true

        ApiClient.instance.getUsers(page = currentPage, perPage = perPage)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    isLoading = false
                    binding.swipeRefreshLayout.isRefreshing = false

                    if (response.isSuccessful) {
                        val users: List<User> = response.body()?.data ?: emptyList()

                        if (currentPage == 1 && users.isEmpty()) {
                            binding.txtEmptyState.visibility = android.view.View.VISIBLE
                            binding.rvUser.visibility = android.view.View.GONE
                        } else {
                            binding.txtEmptyState.visibility = android.view.View.GONE
                            binding.rvUser.visibility = android.view.View.VISIBLE
                            adapter.addAll(users)
                        }

                        val totalPages = response.body()?.total_pages ?: 1
                        isLastPage = currentPage >= totalPages
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    isLoading = false
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this@SelectUserActivity, "Error getting data", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
