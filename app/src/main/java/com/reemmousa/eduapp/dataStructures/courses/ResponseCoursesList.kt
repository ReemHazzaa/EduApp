package com.reemmousa.eduapp.dataStructures.courses

import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

data class ResponseCoursesList(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("results")
    val courses: List<Course?>?
)
