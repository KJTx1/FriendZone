package com.example.friendzone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.*

class PostAdapter(private val initialListOfPosts: List<Post>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private lateinit var parent: Context
    private var listOfPosts: List<Post> = initialListOfPosts.toList()
    private lateinit var reactionAdapter: ReactionAdapter

    var onPostClickListener: ((post: Post) -> Unit)? = null
    var onReactClickListener: ((post: Post) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        this.parent = parent.context
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
        private val btnReact = itemView.findViewById<Button>(R.id.btnReact)
        private val spnReactions = itemView.findViewById<Spinner>(R.id.spnReactions)


        fun bind(post: Post) {
            tvUsername.text = post.user
            Picasso.get().load(post.image).into(ivPostImage)
            tvTimestamp.text = post.timestamp.toString()

            var reactions = arrayOf(HAPPY, LAUGH, SAD, ANGRY, SURPRISE).toMutableList()
            var adapter = ArrayAdapter(parent, android.R.layout.simple_list_item_1, reactions)
            spnReactions.adapter = adapter
            spnReactions.visibility = View.GONE

            itemView.setOnClickListener {
                onPostClickListener?.invoke(post)
            }

            btnReact.setOnClickListener {
                onReactClickListener?.invoke(post)
                toggle(spnReactions)
            }

            if(post.reactions.isNotEmpty()) {
                reactionAdapter = ReactionAdapter(post.reactions)
                rvPostReactions.adapter = reactionAdapter
            }
        }

        private fun toggle(view:View) {
            view.visibility = if (view.visibility == View.VISIBLE){
                View.INVISIBLE
            } else{
                View.VISIBLE
            }
        }
    }

    companion object {
        val HAPPY= getEmoji(0x1F600)
        val LAUGH = getEmoji(0x1F60)
        val SAD = getEmoji(0x1F622)
        val ANGRY = getEmoji(0x1F621)
        val SURPRISE = getEmoji(0x1F62E)

        fun getEmoji(unicode: Int): String {
            return String(Character.toChars(unicode))
        }
    }

}

