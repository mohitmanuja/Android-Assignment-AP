package com.image.assignment.home.model

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("language") var language: String? = null,
    @SerializedName("thumbnail") var thumbnail: Thumbnail? = Thumbnail(),
    @SerializedName("mediaType") var mediaType: Int? = null,
    @SerializedName("coverageURL") var coverageURL: String? = null,
    @SerializedName("publishedAt") var publishedAt: String? = null,
    @SerializedName("publishedBy") var publishedBy: String? = null
) {
    data class Thumbnail(
        @SerializedName("id") var id: String? = null,
        @SerializedName("version") var version: Int? = null,
        @SerializedName("domain") var domain: String? = null,
        @SerializedName("basePath") var basePath: String? = null,
        @SerializedName("key") var key: String? = null,
        @SerializedName("qualities") var qualities: ArrayList<Int> = arrayListOf(),
        @SerializedName("aspectRatio") var aspectRatio: Int? = null

    )
}
