package com.reemmousa.eduapp.ui.allCourses

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.Constants
import com.reemmousa.eduapp.app.Constants.COURSE_PRICE_FREE
import com.reemmousa.eduapp.app.Constants.COURSE_PRICE_PAID
import com.reemmousa.eduapp.app.Constants.DEFAULT_COURSE_CATEGORY
import com.reemmousa.eduapp.app.Constants.QUERY_PRICE_FREE
import com.reemmousa.eduapp.app.Constants.QUERY_PRICE_PAID
import com.reemmousa.eduapp.app.NetworkResult
import com.reemmousa.eduapp.app.hasInternetConnection
import com.reemmousa.eduapp.dataSources.local.room.entities.CoursesEntity
import com.reemmousa.eduapp.dataStructures.courses.ResponseCoursesList
import com.reemmousa.eduapp.dataStructures.filter.FilterPrefs
import com.reemmousa.eduapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
open class AllCoursesViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {
    private val TAG = "AllCoursesViewModel"
    private var coursePrice = COURSE_PRICE_FREE
    private var courseCategory = DEFAULT_COURSE_CATEGORY

    val readPrefs = repository.dataStoreRepository.readPrefs

    val allCoursesResponse: MutableLiveData<NetworkResult<ResponseCoursesList>> = MutableLiveData()

    /** Local DB */

    val coursesInDatabase: LiveData<List<CoursesEntity>> = repository.local.readCourses().asLiveData()

    private fun insertCourses(coursesEntity: CoursesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertCourses(coursesEntity)
        }

    /** Network */

    fun saveAppPrefs(filter: FilterPrefs) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.dataStoreRepository.savePrefs(filter)
        }
    }

    fun getFilteredCourses(filterParameters: FilterParameters) = viewModelScope.launch {
        getCoursesSafeCall(
            searchString = filterParameters.category,
            price = filterParameters.price,
            pageNo = filterParameters.pageNo
        )
    }

    fun filterParamsFromDataStore(): FilterParameters {
        val filter = FilterParameters()
        viewModelScope.launch {
            readPrefs.collect { value ->
                coursePrice = when (value.selectedCoursePrice) {
                    COURSE_PRICE_FREE -> QUERY_PRICE_FREE
                    COURSE_PRICE_PAID -> QUERY_PRICE_PAID
                    else -> ""
                }
                courseCategory = value.selectedCourseCategory
            }
        }
        filter.apply {
            category = courseCategory
            price = coursePrice
            pageNo = 1
        }
        return filter
    }

    fun getAllCourses(pageNo: Int = 1) = viewModelScope.launch {
        getCoursesSafeCall(searchString = Constants.SEARCH_ALL, pageNo = pageNo)
    }

    private suspend fun getCoursesSafeCall(
        searchString: String?,
        pageNo: Int,
        price: String? = null
    ) {
        allCoursesResponse.value = NetworkResult.Loading()

        if (getApplication<Application>().hasInternetConnection()) {
            try {
                val networkResponse = repository.remote.getCourses(searchString, pageNo, price)
                allCoursesResponse.value = handleNetworkResponse(networkResponse)

                val coursesResponse = allCoursesResponse.value!!.data
                if (coursesResponse != null) {
                    offlineCacheCourses(coursesResponse)
                }

            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                NetworkResult.Error(message = e.message, data = null)
            }
        } else {
            allCoursesResponse.value =
                NetworkResult.Error(
                    message = getApplication<Application>().getString(R.string.not_connected),
                    data = null
                )
        }
    }

    private fun offlineCacheCourses(coursesResponse: ResponseCoursesList) {
        val coursesEntity = CoursesEntity(coursesResponse)
        insertCourses(coursesEntity)
    }

    private fun handleNetworkResponse(
        response: Response<ResponseCoursesList?>?
    ): NetworkResult<ResponseCoursesList> {
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

    data class FilterParameters(
        var category: String? = null,
        var price: String? = null,
        var pageNo: Int = 1
    )
}