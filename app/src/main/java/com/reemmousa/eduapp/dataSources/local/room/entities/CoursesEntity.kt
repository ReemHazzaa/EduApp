package com.reemmousa.eduapp.dataSources.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reemmousa.eduapp.app.Constants.COURSES_TABLE
import com.reemmousa.eduapp.dataStructures.courses.ResponseCoursesList

@Entity(tableName = COURSES_TABLE)
class CoursesEntity(
    var responseCoursesList: ResponseCoursesList
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}