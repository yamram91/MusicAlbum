package com.example.musicApp.data

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("results") val results: List<Album>,
    @field:SerializedName("resultCount") val resultCount: Int
)