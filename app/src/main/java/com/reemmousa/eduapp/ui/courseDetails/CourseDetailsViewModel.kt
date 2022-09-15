package com.reemmousa.eduapp.ui.courseDetails

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.NetworkResult
import com.reemmousa.eduapp.app.hasInternetConnection
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.dataStructures.lectures.ResponseCourseLectures
import com.reemmousa.eduapp.dataStructures.reviews.ResponseCourseReview
import com.reemmousa.eduapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    /** Local */
    fun addToWishlist(wishlistEntity: WishlistEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertWishlistEdCourse(wishlistEntity)
        }

    fun addToCart(cartEntity: CartEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertCourseToCart(cartEntity)
        }


    /** Remote */
    val course: MutableLiveData<NetworkResult<Course>> = MutableLiveData()
    val courseLecturesResponse: MutableLiveData<NetworkResult<ResponseCourseLectures>> =
        MutableLiveData()
    val reviewsResponse: MutableLiveData<NetworkResult<ResponseCourseReview>> = MutableLiveData()

    fun getReviews(courseID: String, pageNo: Int) = viewModelScope.launch {
        getReviewsSafeCall(courseID, pageNo)
    }

    private suspend fun getReviewsSafeCall(courseID: String, pageNo: Int) {
        reviewsResponse.value = NetworkResult.Loading()
        if (getApplication<Application>().hasInternetConnection()) {
            try {
                val networkResponse = repository.remote.getCourseReviews(courseID, pageNo)
                reviewsResponse.value = handleNetworkResponse(networkResponse)
            } catch (e: Exception) {
                Log.e("CourseDetailsVM", e.message, e)
                NetworkResult.Error(message = e.message, data = null)
            }
        } else {
            reviewsResponse.value =
                NetworkResult.Error(
                    message = getApplication<Application>().getString(R.string.not_connected),
                    data = null
                )
        }
    }

    private fun handleNetworkResponse(
        response: Response<ResponseCourseReview?>?
    ): NetworkResult<ResponseCourseReview> {
        val code = response?.code()
        val body = response?.body()
        return when {
            code == null || body == null -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.network_error)
                )
            }

            code in 500..599 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.server_error)
                )
            }


            code in 400..499 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.bad_request)
                )
            }

            code in 200..299 && response.isSuccessful -> {
                NetworkResult.Success(body)
            }

            else -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.unknown_error)
                )
            }
        }
    }

    fun getCourseLectures(courseID: String, pageNo: Int) = viewModelScope.launch {
        getLecturesSafeCall(courseID, pageNo)
    }

    private suspend fun getLecturesSafeCall(courseID: String, pageNo: Int) {
        courseLecturesResponse.value = NetworkResult.Loading()
        if (!getApplication<Application>().hasInternetConnection()) {
            courseLecturesResponse.value =
                NetworkResult.Error(
                    message = getApplication<Application>().getString(R.string.not_connected),
                    data = null
                )
        } else {
            try {
                val networkResponse = repository.remote.getCourseLectures(courseID, pageNo)
                courseLecturesResponse.value = handleLecturesNetworkResponse(networkResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResult.Error(message = e.message, data = null)
            }
        }
    }

    private fun handleLecturesNetworkResponse(response: Response<ResponseCourseLectures?>?): NetworkResult<ResponseCourseLectures>? {
        val code = response?.code()
        val body = response?.body()
        return when {
            code == null || body == null -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.network_error)
                )
            }

            code in 500..599 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.server_error)
                )
            }

            code in 400..499 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.bad_request)
                )
            }

            code in 200..299 && response.isSuccessful -> {
                NetworkResult.Success(body)
            }

            else -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.unknown_error)
                )
            }
        }
    }

    fun getCourse(courseID: String) = viewModelScope.launch {
        getCourseSafeCall(courseID)
    }

    private suspend fun getCourseSafeCall(courseID: String) {
        course.value = NetworkResult.Loading()
        if (getApplication<Application>().hasInternetConnection()) {
            try {
                val networkResponse = repository.remote.getCourseDetails(courseID)
                course.value = handleCourseDetailsNetworkResponse(networkResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResult.Error(message = e.message, data = null)
            }
        } else {
            course.value =
                NetworkResult.Error(
                    message = getApplication<Application>().getString(R.string.not_connected),
                    data = null
                )
        }
    }

    private fun handleCourseDetailsNetworkResponse(
        response: Response<Course?>?
    ): NetworkResult<Course>? {
        val code = response?.code()
        val body = response?.body()
        return when {
            code == null || body == null -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.network_error)
                )
            }

            code in 500..599 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.server_error)
                )
            }

            code in 400..499 -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.bad_request)
                )
            }

            code in 200..299 && response.isSuccessful -> {
                NetworkResult.Success(body)
            }

            else -> {
                NetworkResult.Error(
                    data = null,
                    message = getApplication<Application>().getString(R.string.unknown_error)
                )
            }
        }
    }
}