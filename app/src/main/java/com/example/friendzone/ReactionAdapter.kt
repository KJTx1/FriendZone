package com.example.friendzone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.model.Reaction

class ReactionAdapter(private val initialListOfReactions: List<Reaction>): RecyclerView.Adapter<ReactionAdapter.ReactionViewHolder>() {

    private var listOfReactions: List<Reaction> = initialListOfReactions.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionAdapter.ReactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reaction, parent, false)
        return ReactionViewHolder(view)
    }

    override fun getItemCount() = listOfReactions.size

    override fun onBindViewHolder(holder: ReactionAdapter.ReactionViewHolder, position: Int) {
        val reaction: Reaction = listOfReactions[position]
        holder.bind(reaction)
    }

    inner class ReactionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvReactionUser = itemView.findViewById<TextView>(R.id.tvReactionUser)
        private val tvReaction = itemView.findViewById<TextView>(R.id.tvReaction)

        fun bind(reaction: Reaction) {
            tvReactionUser.text = reaction.user
            tvReaction.text = reaction.reaction
        }
    }

}