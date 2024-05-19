package com.image.assignment.home.di


import com.image.assignment.home.MediaCoverageApi
import com.image.assignment.home.repo.MediaCoverageRepo
import com.image.assignment.home.viewModel.HomeViewModel
import com.image.assignment.network.RetrofitServiceHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module

@OptIn(KoinApiExtension::class)
@KoinApiExtension
val homeModule = module {
    single { MediaCoverageRepo(RetrofitServiceHelper().createRetrofitService(MediaCoverageApi::class.java)) }
    viewModel { HomeViewModel(get()) }
}