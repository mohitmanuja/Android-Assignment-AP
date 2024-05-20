package com.image.assignment.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.home.repo.MediaCoverageRepo
import com.image.assignment.network.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repo: MediaCoverageRepo) : ViewModel() {

    private val mediaLiveData: MutableLiveData<Resource<List<MediaResponse>>> = MutableLiveData()

    fun getMediaLiveData(): LiveData<Resource<List<MediaResponse>>> {
        return mediaLiveData
    }

    fun getMediaData(limit: Int) {
        var response: Resource<List<MediaResponse>>?

        viewModelScope.launch {
            mediaLiveData.value = Resource.loading(null)
            withContext(Dispatchers.IO) {
                response = repo.getMediaData(limit)
            }
            withContext(Dispatchers.Main) {
                response?.run {
                    mediaLiveData.value = this
                }
            }
        }
    }

}