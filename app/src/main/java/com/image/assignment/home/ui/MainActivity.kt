package com.image.assignment.home.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.image.assignment.R
import com.image.assignment.databinding.ActivityMainBinding
import com.image.assignment.home.viewModel.HomeViewModel
import com.image.assignment.network.data.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        init()
    }

    private fun init() {
        homeViewModel.getMediaData(100)
        setObservers()
    }

    private fun setObservers() {
        homeViewModel.getMediaLiveData().observe(this) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    binding?.loadingGroup?.visibility = View.VISIBLE

                }

                Resource.Status.SUCCESS -> {
                    binding?.loadingGroup?.visibility = View.GONE
                    binding?.recyclerview?.visibility = View.VISIBLE


                }

                Resource.Status.ERROR -> {
                    binding?.apply {
                        loadingGroup.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        loadingLabel.text = getString(R.string.something_went_wrong)
                    }
                }
            }
        }
    }
}