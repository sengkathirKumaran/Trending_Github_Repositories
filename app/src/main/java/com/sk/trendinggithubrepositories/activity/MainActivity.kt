package com.sk.trendinggithubrepositories.activity

import android.app.Activity
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sk.trendinggithubrepositories.*
import com.sk.trendinggithubrepositories.adapter.RepositoryAdapter
import com.sk.trendinggithubrepositories.data.RepositoryItem
import com.sk.trendinggithubrepositories.datastore.LocalDataStore
import com.sk.trendinggithubrepositories.repository.GithubRepository
import com.sk.trendinggithubrepositories.viewmodel.GithubViewModel
import com.sk.trendinggithubrepositories.viewmodel.GithubViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_item.*
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var mGithubViewModel: GithubViewModel
    private val repositoryAdapter by lazy { RepositoryAdapter() }
    private lateinit var localDataStore: LocalDataStore
    private val gson = Gson()
    private val type = object :
        TypeToken<ArrayList<RepositoryItem>>() {}.type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        localDataStore = LocalDataStore(this)

        setupRecyclerView()

        val repository = GithubRepository(this@MainActivity)
        val viewModelFactory = GithubViewModelFactory(repository)
        mGithubViewModel =
            ViewModelProvider(this, viewModelFactory).get(GithubViewModel::class.java)

        if (isNetworkAvailable(this)) {
            progressBar.visibility = View.VISIBLE
            retrieveRemoteData()
        } else {
            Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
            retrieveLocalData("No network connection")
        }

        mGithubViewModel.mResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                progressBar.visibility = View.GONE
                swipeRefreshLayout.isRefreshing = false
                txt_no_connection.visibility = View.GONE
                btn_try_again.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                repositoryAdapter.setData(response.body()!!)
            } else {
                Toast.makeText(this, "Request failed", Toast.LENGTH_SHORT).show()
                retrieveLocalData("Request Failed")
            }
        })

        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            if (isNetworkAvailable(this)) {
                swipeRefreshLayout.isRefreshing = true
                retrieveRemoteData()
            } else {
                Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
                retrieveLocalData("No network connection")
            }
        })


        btn_try_again.setOnClickListener {
            if (isNetworkAvailable(this)) {
                progressBar.visibility = View.VISIBLE
                retrieveRemoteData()
            } else {
                Toast.makeText(this, "No network connection", Toast.LENGTH_SHORT).show()
                retrieveLocalData("No network connection")
            }
        }

    }


    private fun retrieveLocalData(text: String) {

        progressBar.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        lifecycleScope.launch {

            if (localDataStore.read().equals(null, true)) {
                recyclerView.visibility = View.GONE
                txt_no_connection.visibility = View.VISIBLE
                txt_no_connection.text = text
                btn_try_again.visibility = View.VISIBLE
            } else {
                txt_no_connection.visibility = View.GONE
                btn_try_again.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                repositoryAdapter.setData(gson.fromJson(localDataStore.read(), type))
            }
        }
    }

    private fun retrieveRemoteData() {
        txt_no_connection.visibility = View.GONE
        btn_try_again.visibility = View.GONE
        mGithubViewModel.fetchData()
    }


    private fun isNetworkAvailable(context: Activity): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected

    }

    private fun setupRecyclerView() {
        recyclerView.adapter = repositoryAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                repositoryAdapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
