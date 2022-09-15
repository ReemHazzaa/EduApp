package com.reemmousa.eduapp.ui.courseDetails.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.dataStructures.reviews.Review

class ReviewsAdapter :
    RecyclerView.Adapter<ReviewsAdapter.CustomVH>() {

    private var data: List<Review?>? = null

    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivUser: ImageView = itemView.findViewById(R.id.iv_user)
        val tvReviewerName: TextView = itemView.findViewById(R.id.tv_reviewer_name)
        val tvReviewContent: TextView = itemView.findViewById(R.id.tv_review_content)
        val ratingBar: RatingBar = itemView.findViewById(R.id.rating_bar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return CustomVH(view)
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        val current = data?.get(position)
        holder.apply {
            tvReviewerName.text = current?.user?.displayName.toString()
            val content = current?.content.toString()
            if (content.isEmpty()) {
                tvReviewContent.visibility = View.GONE
            } else {
                tvReviewContent.visibility = View.VISIBLE
                tvReviewContent.text = current?.content.toString()
            }
            ratingBar.rating = current?.rating?.toFloat() ?: 1f
        }

    }

    override fun getItemCount(): Int = data?.size ?: 0

    fun setData(list: List<Review?>?) {
        data = list
    }
}