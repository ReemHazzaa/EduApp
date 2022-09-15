package com.reemmousa.eduapp.dataStructures.reviews


import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("_class")
    val classX: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("modified")
    val modified: String?,
    @SerializedName("rating")
    val rating: Float?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("user_modified")
    val userModified: String?
)