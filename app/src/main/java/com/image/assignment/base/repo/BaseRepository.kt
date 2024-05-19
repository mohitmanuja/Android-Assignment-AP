package com.image.assignment.base.repo

import com.image.assignment.network.data.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


open class BaseRepository {

    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.success(apiCall.invoke())
            } catch (throwable: Throwable) {
                Resource.loading(false)
                when (throwable) {
                    is IOException -> Resource.error(throwable, null)
                    is HttpException -> {
                        Resource.error(throwable, null)
                    }
                    is UnknownHostException -> Resource.error(throwable, null)
                    is Exception -> Resource.error(throwable, null)
                    else -> Resource.error(throwable, null)
                }
            }
        }
    }
}