package com.reemmousa.eduapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.loadImageWithPicasso
import com.reemmousa.eduapp.app.viewLinkOnUdemy
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.ui.home.HomeFragmentDirections


class HomeCoursesAdapter(
    private val navController: NavController
) :
    RecyclerView.Adapter<HomeCoursesAdapter.CustomVH>() {
    private var coursesList: List<Course?>? = null

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView
        var tvName: TextView
        var courseOnUdemy: Button
        var tvInstructorName: TextView
        var ivInstructor: ImageView
        var tvCoursePrice: TextView

        init {
            ivImage = itemView.findViewById(R.id.iv_course_image)
            tvName = itemView.findViewById(R.id.tv_course_name)
            courseOnUdemy = itemView.findViewById(R.id.coursesOnUdemy)
            tvInstructorName = itemView.findViewById(R.id.tv_instructors)
            ivInstructor = itemView.findViewById(R.id.iv_instructor)
            tvCoursePrice = itemView.findViewById(R.id.tv_course_price)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_home, parent, false)
        return CustomVH(view)
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        val context = holder.itemView.context
        val current = coursesList?.get(position)

        holder.apply {
            tvName.text = current?.title.toString()
            tvCoursePrice.text = current?.price.toString()
            tvInstructorName.text =
                current?.visibleInstructors?.getOrNull(0)?.displayName.toString()

            courseOnUdemy.setOnClickListener {
                context.viewLinkOnUdemy(current?.url.toString())
            }
            current?.image480x270?.let { ivImage.loadImageWithPicasso(it) }
            current?.visibleInstructors?.getOrNull(0)?.image100x100?.let {
                ivInstructor.loadImageWithPicasso(
                    it
                )
            }

            itemView.setOnClickListener {
                if (current != null) {
                    val action =
                        HomeFragmentDirections.actionNavigationHomeToCourseDetailsFragment(
                            selectedCourse = current
                        )
                    navController.navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int = coursesList?.size ?: 0

    fun setData(data: List<Course?>?) {
        coursesList = data
        notifyDataSetChanged()
    }
}