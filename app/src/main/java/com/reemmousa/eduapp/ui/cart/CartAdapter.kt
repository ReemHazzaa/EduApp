package com.reemmousa.eduapp.ui.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.loadImageWithPicasso
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity

open class CartAdapter(private val cartViewModel: CartViewModel) :
    RecyclerView.Adapter<CartAdapter.MainCoursesVH>() {

    private var data: List<CartEntity?>? = mutableListOf()

    class MainCoursesVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView = itemView.findViewById(R.id.tv_course_name)
        val tvInstructors: TextView = itemView.findViewById(R.id.tv_instructors)
        val tvCoursePrice: TextView = itemView.findViewById(R.id.tv_course_price)
        val ivCourse: ImageView = itemView.findViewById(R.id.iv_course)
        val ivInstructor: ImageView = itemView.findViewById(R.id.iv_instructor)
        val btBuy: Button = itemView.findViewById(R.id.bt_buy)
        val btRemove: Button = itemView.findViewById(R.id.bt_remove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainCoursesVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_cart, parent, false)
        return MainCoursesVH(view)
    }

    override fun onBindViewHolder(holder: MainCoursesVH, position: Int) {
        val current = data?.get(position)
        holder.apply {
            tvCourseName.text = current?.course?.title.toString()
            tvInstructors.text =
                current?.course?.visibleInstructors?.getOrNull(0)?.displayName ?: ""
            tvCoursePrice.text = current?.course?.price.toString()
            ivCourse.loadImageWithPicasso(current?.course?.image240x135.toString())
            ivInstructor.loadImageWithPicasso(
                current?.course?.visibleInstructors?.getOrNull(0)?.image50x50.toString()
            )
            btRemove.setOnClickListener {
                if (current != null) {
                    cartViewModel.deleteCourseFromCart(current)
                }
            }
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun setData(list: List<CartEntity?>?) {
        data = list
    }
}