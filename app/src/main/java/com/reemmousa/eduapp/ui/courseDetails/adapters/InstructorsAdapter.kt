package com.reemmousa.eduapp.ui.courseDetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.loadImageWithPicasso
import com.reemmousa.eduapp.app.viewLinkOnUdemy
import com.reemmousa.eduapp.dataStructures.courses.VisibleInstructor


class InstructorsAdapter :
    RecyclerView.Adapter<InstructorsAdapter.CustomVH>() {
    private var instructors: MutableList<VisibleInstructor?>? = null

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivInstructor: ImageView
        var tvInstructorName: TextView
        var tvJob: TextView
        var tvInstructorOnUdemy: TextView

        init {
            ivInstructor = itemView.findViewById(R.id.iv_instructor)
            tvInstructorName = itemView.findViewById(R.id.tv_instructor_name)
            tvJob = itemView.findViewById(R.id.tv_instructor_job)
            tvInstructorOnUdemy = itemView.findViewById(R.id.instructor_on_udemy)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instructor, parent, false)
        return CustomVH(view)
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        val context = holder.itemView.context
        val current = instructors?.get(position)

        holder.apply {
            tvInstructorName.text = current?.displayName.toString()
            tvJob.text = current?.jobTitle.toString()

            tvInstructorOnUdemy.setOnClickListener {
                context.viewLinkOnUdemy(current?.url.toString())
            }
            current?.image100x100?.let { ivInstructor.loadImageWithPicasso(it) }
        }
    }

    override fun getItemCount(): Int = instructors?.size ?: 0

    fun setData(list: MutableList<VisibleInstructor?>?) {
        instructors = list
    }
}