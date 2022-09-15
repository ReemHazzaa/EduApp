package com.reemmousa.eduapp.ui.wishlist

import com.reemmousa.eduapp.ui.adapters.CourseItemOperations
import com.reemmousa.eduapp.ui.adapters.CoursesMainAdapter

class WishlistAdapter(private val courseItemOperations: CourseItemOperations) :
    CoursesMainAdapter(courseItemOperations) {

    override fun onBindViewHolder(holder: MainCoursesVH, position: Int) {
        super.onBindViewHolder(holder, position)
        courseItemOperations.onCourseBindViewHolder(holder)
    }

}