package com.brian.jotz.data.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brian.jotz.data.database.entities.Jotz
import com.brian.jotz.databinding.JotListItemBinding

class JotItemsAdapter(
    val onEachItem: (Any, View) -> Unit,
    @LayoutRes val holderView: Int
) : RecyclerView.Adapter<JotItemsAdapter.JotItemViewHolder>() {

    private var dataList: List<Any> = emptyList()

    class JotItemViewHolder(itemView: View, val onEachItem: (Any, View) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: Any) {
            onEachItem(item, itemView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JotItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(holderView, parent, false)

        return JotItemViewHolder(view, onEachItem)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: JotItemViewHolder, position: Int) {
        val item = dataList.get(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setData(newDataList: List<Any>) {
        this.dataList = newDataList
        notifyDataSetChanged()
    }

}

//class JotzListAdapter(private val onItemClicked: (Jotz) -> Unit) :
//    ListAdapter<Jotz, JotzListAdapter.JotzViewHolder>(DiffCallback) {
//
//    class JotzViewHolder(private var binding: JotListItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        fun bind(jotz: Jotz) {
//            binding.apply {
//                jotListTitle.text = jotz.jotzTitle.toString()
//                jotListDesc.text = jotz.jotzBody.toString()
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JotzViewHolder {
//        return JotzViewHolder(
//            JotListItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: JotzViewHolder, position: Int) {
//
//        val current = getItem(position)
//        holder.itemView.setOnClickListener {
//            onItemClicked(current)
//        }
//        holder.bind(current)
//    }
//
//    companion object {
//        private val DiffCallback = object : DiffUtil.ItemCallback<Jotz>() {
//            override fun areItemsTheSame(oldItem: Jotz, newItem: Jotz): Boolean {
//                return oldItem == newItem
//            }
//
//            override fun areContentsTheSame(oldItem: Jotz, newItem: Jotz): Boolean {
//                return oldItem.jotzTitle == newItem.jotzTitle
//            }
//
//        }
//    }
//
//
//}