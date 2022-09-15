package com.reemmousa.eduapp.dataStructures.lectures


import com.google.gson.annotations.SerializedName

data class ResponseCourseLectures(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("results")
    val lectures: List<Lecture?>?
)