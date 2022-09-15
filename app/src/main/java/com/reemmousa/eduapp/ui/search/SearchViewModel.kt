package com.reemmousa.eduapp.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.NetworkResult
import com.reemmousa.eduapp.app.hasInternetConnection
import com.reemmousa.eduapp.dataStructures.courses.ResponseCoursesList
import com.reemmousa.eduapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val selectedTopSearchString: MutableLiveData<String> = MutableLiveData()

    val topSearchesList: List<String> = repository.constants.topSearchList

    val searchResultCourses: MutableLiveData<NetworkResult<ResponseCoursesList>> = MutableLiveData()

    fun searchCourses(searchKeyword: String) = viewModelScope.launch {
        searchCoursesSafeCall(searchKeyword, 1)
    }

    private suspend fun searchCoursesSafeCall(searchKeyword: String, pageNo: Int) {
        searchResultCourses.value = NetworkResult.Loading()

        if (getApplication<Application>().hasInternetConnection()) {
            try {
                val networkResponse = repository.remote.
                getCourses(searchKeyword, pageNo)
                searchResultCourses.value = handleNetworkResponse(networkResponse)
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResult.Error(message = e.message, data = null)
            }
        } else {
            searchResultCourses.value =
                NetworkResult.Error(
                    message = getApplication<Application>().getString(R.string.not_connected),
                    data = null
                )
        }
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
}