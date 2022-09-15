package com.reemmousa.eduapp.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val readCartFromDB: LiveData<List<CartEntity>> =
        repository.local.readCart().asLiveData()

    fun deleteAllCart() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllCart()
    }

    fun deleteCourseFromCart(cartEntity: CartEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteCourseFromCart(cartEntity)
        }

}