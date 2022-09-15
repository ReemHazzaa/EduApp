package com.reemmousa.eduapp.dataSources.local.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.dataStructures.courses.ResponseCoursesList

class CoursesTypeConverter {
    var gson = Gson()

    @TypeConverter
    fun responseCoursesListToString(responseCoursesList: ResponseCoursesList): String {
        return gson.toJson(responseCoursesList)
    }

    @TypeConverter
    fun stringToResponseCoursesList(data: String): ResponseCoursesList {
        val listType = object : TypeToken<ResponseCoursesList>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun courseToString(course: Course): String? {
        return gson.toJson(course)
    }

    @TypeConverter
    fun stringToCourse(data: String): Course {
        val listType = object : TypeToken<Course>() {}.type
        return gson.fromJson(data, listType)
    }
}