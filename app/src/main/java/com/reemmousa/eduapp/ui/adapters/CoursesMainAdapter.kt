package com.reemmousa.eduapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.loadImageWithPicasso
import com.reemmousa.eduapp.dataStructures.courses.Course

open class CoursesMainAdapter(private val courseItemOperations: CourseItemOperations) :
    RecyclerView.Adapter<CoursesMainAdapter.MainCoursesVH>() {

    private var data: List<Course?>? = mutableListOf()

    class MainCoursesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        val tvInstructors: TextView = itemView.findViewById(R.id.tv_instructors)
        val tvCoursePrice: TextView = itemView.findViewById(R.id.tv_course_price)
        val ivCourse: ImageView = itemView.findViewById(R.id.iv_course)
        val ivInstructor: ImageView = itemView.findViewById(R.id.iv_instructor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCoursesVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_courses_list, parent, false)
        return MainCoursesVH(view)
    }

    override fun onBindViewHolder(holder: MainCoursesVH, position: Int) {
        val current = data?.get(position)
        holder.apply {
            tvCourseName.text = current?.title.toString()
            tvInstructors.text = current?.visibleInstructors?.getOrNull(0)?.displayName ?: ""
            tvCoursePrice.text = current?.price.toString()
            ivCourse.loadImageWithPicasso(current?.image240x135.toString())
            ivInstructor.loadImageWithPicasso(
                current?.visibleInstructors?.getOrNull(0)?.image50x50.toString()
            )
            itemView.setOnClickListener {
                if (current != null) {
                    courseItemOperations.onCourseClicked(current)
                    courseItemOperations.onCourseClicked(position, current, holder)
                }
            }
            itemView.setOnLongClickListener {
                if (current != null) {
                    courseItemOperations.onCourseLongClicked(position, current, holder)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun setData(list: List<Course?>?) {
        data = list
    }
}