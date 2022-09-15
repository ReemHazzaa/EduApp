package com.reemmousa.eduapp.ui.search.adapters

import com.reemmousa.eduapp.ui.adapters.CourseItemOperations
import com.reemmousa.eduapp.ui.adapters.CoursesMainAdapter

class ResultSearchAdapter(courseItemOperations: CourseItemOperations) :
    CoursesMainAdapter(courseItemOperations) {


//    override fun onBindViewHolder(holder: TopSearchVH, position: Int) {
//        val current = data?.get(position)
//        holder.apply {
//            tvCourseName.text = current?.title.toString()
//            tvInstructors.text = current?.visibleInstructors?.getOrNull(0)?.displayName ?: ""
//            tvCoursePrice.text = current?.price.toString()
//            ivCourse.loadImageWithPicasso(current?.image240x135.toString())
//            ivInstructor.loadImageWithPicasso(
//                current?.visibleInstructors?.getOrNull(0)?.image50x50.toString()
//            )
//            itemView.setOnClickListener {
//                if (current != null) {
//                    var action =
//                        SearchFragmentDirections.actionSearchFragmentToCourseDetailsFragment(current)
//                    try {
//                        navController.navigate(action)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        action =
//                            AllCoursesFragmentDirections.actionAllCoursesFragmentToCourseDetailsFragment(
//                                current
//                            )
//                        try {
//                            navController.navigate(action)
//                        } catch (e: java.lang.Exception) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            }
//        }
//    }

}