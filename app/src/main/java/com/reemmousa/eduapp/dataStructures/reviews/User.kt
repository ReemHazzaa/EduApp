package com.reemmousa.eduapp.dataStructures.reviews


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_class")
    val classX: String?,
    @SerializedName("display_name")
    val displayName: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("title")
    val title: String?
)