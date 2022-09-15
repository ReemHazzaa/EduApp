package com.reemmousa.eduapp.dataSources.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.CoursesEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity

@Database(
    entities = [CoursesEntity::class, WishlistEntity::class, CartEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(CoursesTypeConverter::class)
abstract class CoursesDatabase : RoomDatabase() {
    abstract fun coursesDao(): CoursesDao
}