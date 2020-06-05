package com.example.friendzone

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.GroupAdapter.GroupViewHolder
import com.example.friendzone.model.Group

class GroupAdapter(private val initialListOfGroups: List<Group>): RecyclerView.Adapter<GroupViewHolder>() {

    private var listOfGroups: List<Group> = initialListOfGroups.toList()
    var onGroupClickListener: ((group: Group) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.group, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount() = listOfGroups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group: Group = listOfGroups[position]
        holder.bind(group)
    }

    inner class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvGroupName = itemView.findViewById<TextView>(R.id.tvGroupName)

        fun bind(group: Group) {
            tvGroupName.text = group.name
            itemView.setOnClickListener {
                onGroupClickListener?.invoke(group)
            }

        }
    }

}