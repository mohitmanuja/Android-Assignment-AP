package com.image.assignment.home.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.image.assignment.R
import com.image.assignment.databinding.ActivityMainBinding
import com.image.assignment.home.adapter.HomeAdapter
import com.image.assignment.home.viewModel.HomeViewModel
import com.image.assignment.network.data.Resource
import com.image.assignment.utils.NetworkUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var binding: ActivityMainBinding? = null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        init()
    }

    private fun init() {
        if (!NetworkUtil.isInternetAvailable(this)) {
            Toast.makeText(this,getString(R.string.network_not_available_cached),Toast.LENGTH_LONG).show()
        }
        homeViewModel.getMediaData(100)
        setObservers()
    }

    private fun setObservers() {
        homeViewModel.getMediaLiveData().observe(this) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding?.apply {
                        loadingLabel.visibility = View.VISIBLE
                        loadingLabel.text = getString(R.string.loading_data_please_wait)
                        progressBar.visibility = View.VISIBLE
                        retryButton.visibility = View.GONE
                    }

                }

                Resource.Status.SUCCESS -> {

                    val adapter = it.data?.let { it1 -> HomeAdapter(this, it1, scope) }
                    binding?.apply {
                        loadingLabel.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        recyclerview.visibility = View.VISIBLE
                        recyclerview.adapter = adapter
                    }
                }

                Resource.Status.ERROR -> {
                    binding?.apply {
                        loadingLabel.visibility = View.VISIBLE
                        retryButton.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        loadingLabel.text = getString(R.string.something_went_wrong)
                        retryButton.setOnClickListener {
                            homeViewModel.getMediaData(100)
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}