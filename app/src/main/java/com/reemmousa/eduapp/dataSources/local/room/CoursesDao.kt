package com.reemmousa.eduapp.dataSources.local.room

import androidx.room.*
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.CoursesEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(coursesEntity: CoursesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseToWishlistTable(wishlistEntity: WishlistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseToCartTable(cartEntity: CartEntity)

    @Query("SELECT * FROM courses_table")
    fun readCourses(): Flow<List<CoursesEntity>>

    @Query("SELECT * FROM wishlist_table ORDER BY id ASC")
    fun readCoursesWishlist(): Flow<List<WishlistEntity>>

    @Query("SELECT * FROM cart_table ORDER BY id ASC")
    fun readCoursesCart(): Flow<List<CartEntity>>

    @Delete
    suspend fun deleteWishlistEdCourse(wishlistEntity: WishlistEntity)

    @Delete
    suspend fun deleteCourseFromCart(cartEntity: CartEntity)

    @Query("DELETE FROM wishlist_table")
    suspend fun deleteAllWishlist()

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCart()
}