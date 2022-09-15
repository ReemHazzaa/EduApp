package com.reemmousa.eduapp.ui.wishlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import com.reemmousa.eduapp.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    val readWishlistFromDB: LiveData<List<WishlistEntity>> =
        repository.local.readWishlist().asLiveData()

    fun deleteAllWishlist() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllWishlist()
    }

    fun deleteCourseFromWishlist(wishlistEntity: WishlistEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteCourseFromWishlist(wishlistEntity)
        }

}