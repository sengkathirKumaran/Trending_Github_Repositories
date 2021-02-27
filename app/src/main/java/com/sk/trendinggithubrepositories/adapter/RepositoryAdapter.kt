package com.sk.trendinggithubrepositories.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sk.trendinggithubrepositories.R
import com.sk.trendinggithubrepositories.data.RepositoryItem
import kotlinx.android.synthetic.main.recycler_item.view.*

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>(), Filterable {

    private var repositoryList = ArrayList<RepositoryItem>()
    private var repositoryListAll = ArrayList<RepositoryItem>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.itemView)
            .load(repositoryList[position].avatar)
            .into(holder.itemView.image)

        holder.itemView.repo_language_color.setColorFilter(
            Color.parseColor(repositoryList[position].languageColor)
        )
        holder.itemView.repo_author.text = repositoryList[position].author
        holder.itemView.repo_name.text = repositoryList[position].name
        holder.itemView.repo_description.text = repositoryList[position].description
        holder.itemView.repo_stars.text = repositoryList[position].stars.toString()
        holder.itemView.repo_language.text = repositoryList[position].language
    }


    fun setData(repo: ArrayList<RepositoryItem>) {
        this.repositoryList = repo
        repositoryListAll.clear()
        repositoryListAll.addAll(repo)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return this.repositoryList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                var filteredList = ArrayList<RepositoryItem>()

                if (charSequence.toString().isEmpty()) {
                    filteredList.addAll(repositoryListAll)
                } else {
                    for (repo in repositoryListAll) {
                        if (repo.name.toLowerCase()
                                .contains(charSequence.toString().toLowerCase())
                        ) {
                            filteredList.add(repo)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                repositoryList.clear()
                repositoryList.addAll(filterResults?.values as Collection<RepositoryItem>)
                notifyDataSetChanged()
            }
        }
    }
}
