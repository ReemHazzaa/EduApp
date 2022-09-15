package com.reemmousa.eduapp.ui.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.dataStructures.HomeCategory
import com.reemmousa.eduapp.ui.home.HomeFragmentDirections
import com.reemmousa.eduapp.ui.search.SearchFragmentDirections
import com.reemmousa.eduapp.ui.search.SearchViewModel


class HomeCategoriesAdapter(
    private val categoriesList: List<HomeCategory>,
    private val navController: NavController
) :
    RecyclerView.Adapter<HomeCategoriesAdapter.CustomVH>() {
    class CustomVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage: ImageView
        var tvName: TextView

        init {
            ivImage = itemView.findViewById(R.id.iv_image)
            tvName = itemView.findViewById(R.id.tv_name)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_category_home, parent, false)
        return CustomVH(view)
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        val current = categoriesList[position]

        holder.apply {
            tvName.text = current.catName
            ivImage.setImageDrawable(current.catPic)
            itemView.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToSearchFragment(current.catName)
                navController.navigate(action)
            }
        }
    }

    override fun getItemCount(): Int = categoriesList.size
}