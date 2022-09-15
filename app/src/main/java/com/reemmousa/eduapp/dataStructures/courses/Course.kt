package com.reemmousa.eduapp.dataStructures.courses


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    @SerializedName("id")
    val courseId: Int?,
    @SerializedName("headline")
    val headline: String?,
    @SerializedName("image_125_H")
    val image125H: String?,
    @SerializedName("image_240x135")
    val image240x135: String?,
    @SerializedName("image_480x270")
    val image480x270: String?,
    @SerializedName("is_paid")
    val isPaid: Boolean?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("price_serve_tracking_id")
    val priceServeTrackingId: String?,
    @SerializedName("published_title")
    val publishedTitle: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("tracking_id")
    val trackingId: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("visible_instructors")
    val visibleInstructors: MutableList<VisibleInstructor?>?,
) : Parcelable