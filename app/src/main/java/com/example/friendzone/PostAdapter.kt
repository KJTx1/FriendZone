package com.example.friendzone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_group_feed.*

class PostAdapter(private val initialListOfPosts: List<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var listOfPosts: List<Post> = initialListOfPosts.toList()
    private lateinit var reactionAdapter: ReactionAdapter

    var onPostClickListener: ((post: Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = listOfPosts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: Post = listOfPosts[position]
        holder.bind(post)
    }

    fun change(newPosts: List<Post>) {
        listOfPosts = newPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        private val ivPostImage = itemView.findViewById<ImageView>(R.id.ivPostImage)
        private val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
        private val rvPostReactions = itemView.findViewById<RecyclerView>(R.id.rvPostReactions)

        fun bind(post: Post) {
            tvUsername.text = post.user
            Picasso.get().load(post.image).into(ivPostImage)
            tvTimestamp.text = post.timestamp.toString()

            itemView.setOnClickListener {
                onPostClickListener?.invoke(post)
            }
            if(post.reactions.isNotEmpty()) {
                reactionAdapter = ReactionAdapter(post.reactions)
                rvPostReactions.adapter = reactionAdapter
            }
        }
    }

}

