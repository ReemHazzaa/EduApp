package com.reemmousa.eduapp.dataStructures.lectures


import com.google.gson.annotations.SerializedName

data class Lecture(
    @SerializedName("_class")
    val classX: String?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_published")
    val isPublished: Boolean?,
    @SerializedName("sort_order")
    val sortOrder: Int?,
    @SerializedName("title")
    val title: String?
)