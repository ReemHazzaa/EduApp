package com.reemmousa.eduapp.dataSources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reemmousa.eduapp.app.Constants.CART_TABLE
import com.reemmousa.eduapp.app.Constants.COURSES_TABLE
import com.reemmousa.eduapp.app.Constants.WISHLIST_TABLE
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.dataStructures.courses.ResponseCoursesList

@Entity(tableName = CART_TABLE)
class CartEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var course: Course
)