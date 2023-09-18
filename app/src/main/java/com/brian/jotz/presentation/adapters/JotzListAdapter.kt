package com.brian.jotz.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brian.jotz.data.models.Jotz
import com.brian.jotz.databinding.JotListItemBinding

class JotzListAdapter(private val onItemClicked: (Jotz) -> Unit) :
    ListAdapter<Jotz, JotzListAdapter.JotzViewHolder>(DiffCallback) {

    class JotzViewHolder(private var binding: JotListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jotz: Jotz) {
            binding.apply {
                jotListTitle.text = jotz.jotzTitle.toString()
                jotListDesc.text = jotz.jotzBody.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JotzViewHolder {
        return JotzViewHolder(
            JotListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: JotzViewHolder, position: Int) {

        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Jotz>() {
            override fun areItemsTheSame(oldItem: Jotz, newItem: Jotz): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Jotz, newItem: Jotz): Boolean {
                return oldItem.jotzTitle == newItem.jotzTitle
            }

        }
    }


}