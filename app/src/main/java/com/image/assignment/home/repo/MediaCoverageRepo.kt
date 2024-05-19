package com.image.assignment.home.repo


import com.image.assignment.base.repo.BaseRepository
import com.image.assignment.home.model.MediaResponse
import com.image.assignment.home.MediaCoverageApi
import com.image.assignment.network.data.Resource

class MediaCoverageRepo(val api: MediaCoverageApi) : BaseRepository() {
    suspend fun getMediaData(limit: Int): Resource<List<MediaResponse>> {
        return safeApiCall { api.getMediaResponse(limit) }
    }

}

