package com.reemmousa.eduapp.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.ui.search.SearchViewModel

class TopSearchAdapter(private val data: List<String>, private val searchViewModel: SearchViewModel) : RecyclerView.Adapter<TopSearchAdapter.TopSearchVH>() {

    class TopSearchVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTV: TextView = itemView.findViewById(R.id.tv_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopSearchVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_search, parent, false)
        return TopSearchVH(view)
    }

    override fun onBindViewHolder(holder: TopSearchVH, position: Int) {
        val current = data[position]
        holder.titleTV.text = current
        holder.itemView.setOnClickListener {
            searchViewModel.selectedTopSearchString.value = current
        }
    }

    override fun getItemCount(): Int = data.size
}