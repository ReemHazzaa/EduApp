package com.reemmousa.eduapp.dataSources.local

import com.reemmousa.eduapp.dataSources.local.room.CoursesDao
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.CoursesEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val coursesDao: CoursesDao
) {
    suspend fun insertCourses(coursesEntity: CoursesEntity) {
        coursesDao.insertCourses(coursesEntity)
    }

    fun readCourses(): Flow<List<CoursesEntity>> {
        return coursesDao.readCourses()
    }

    fun readWishlist(): Flow<List<WishlistEntity>> {
        return coursesDao.readCoursesWishlist()
    }

    suspend fun insertWishlistEdCourse(wishlistEntity: WishlistEntity) {
        coursesDao.insertCourseToWishlistTable(wishlistEntity)
    }

    suspend fun deleteCourseFromWishlist(wishlistEntity: WishlistEntity) {
        coursesDao.deleteWishlistEdCourse(wishlistEntity)
    }

    suspend fun deleteAllWishlist() {
        coursesDao.deleteAllWishlist()
    }

    fun readCart(): Flow<List<CartEntity>> {
        return coursesDao.readCoursesCart()
    }

    suspend fun insertCourseToCart(cartEntity: CartEntity) {
        coursesDao.insertCourseToCartTable(cartEntity)
    }

    suspend fun deleteCourseFromCart(cartEntity: CartEntity) {
        coursesDao.deleteCourseFromCart(cartEntity)
    }

    suspend fun deleteAllCart() {
        coursesDao.deleteAllCart()
    }
}