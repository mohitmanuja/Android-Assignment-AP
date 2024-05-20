package com.image.assignment.home

import com.image.assignment.home.model.MediaResponse
import com.image.assignment.network.NetworkConfig
import retrofit2.http.*

interface MediaCoverageApi {

    @GET(NetworkConfig.MEDIA_COVERAGE)
    suspend fun getMediaResponse(@Query("limit") limit: Int): List<MediaResponse>
}

