package com.example.friendzone

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class PostAdapter(private val initialListOfPosts: List<UploadManager>): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private lateinit var parent: Context
    private var listOfPosts: List<UploadManager> = initialListOfPosts.toList()
    private lateinit var reactionAdapter: ReactionAdapter

    var onPostClickListener: ((post: UploadManager) -> Unit)? = null

    private var touchX: Int? = null
    private var touchY: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        this.parent = parent.context
        return PostViewHolder(view)
    }

    override fun getItemCount() = listOfPosts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post: UploadManager = listOfPosts[position]
        holder.bind(post)
    }


    fun change(newPosts: List<UploadManager>) {
        listOfPosts = newPosts
        notifyDataSetChanged()
    }

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        private val ivPostImage = itemView.findViewById<ImageView>(R.id.ivPostImage)
        private val tvTimestamp = itemView.findViewById<TextView>(R.id.tvTimestamp)
        private val rvPostReactions = itemView.findViewById<RecyclerView>(R.id.rvPostReactions)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: UploadManager) {
            tvUsername.text = post.userName
            Picasso.get().load(post.imageUrl).into(ivPostImage)
            tvTimestamp.text = convertLongToTime(post.timeStamp!!)


            var reactions = arrayOf(HAPPY, LAUGH, SAD, ANGRY, SURPRISE).toMutableList()
            var adapter = ArrayAdapter(parent, android.R.layout.simple_list_item_1, reactions)

            itemView.setOnClickListener {
                onPostClickListener?.invoke(post)
            }

            ivPostImage.setOnTouchListener { v, event ->
                touchX = event.rawX.toInt()
                touchY = event.rawY.toInt()

                false
            }

            ivPostImage.setOnLongClickListener {
                createPopupWindow(position).showAtLocation(ivPostImage, 0, touchX!!, touchY!!)

                true
            }

            if(post.reaction.isNotEmpty()) {
                reactionAdapter = ReactionAdapter(post.reaction)
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

    private fun createPopupWindow(itemPosition: Int): PopupWindow {
        val popupMenuView = LayoutInflater.from(parent).inflate(R.layout.emoji_selection_popup, null).apply {
            findViewById<ImageView>(R.id.btnHairFace).setOnClickListener {
                Toast.makeText(context, "Hair Emoji has been clicked for position: $itemPosition", Toast.LENGTH_SHORT).show()
            }
            findViewById<ImageView>(R.id.btnGroup).setOnClickListener {
                Toast.makeText(context, "Group Emoji has been clicked for position: $itemPosition", Toast.LENGTH_SHORT).show()
            }
            findViewById<ImageView>(R.id.btnSmiley).setOnClickListener {
                Toast.makeText(context, "Smiley Emoji has been clicked for position: $itemPosition", Toast.LENGTH_SHORT).show()
            }
        }

        return PopupWindow(popupMenuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

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

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        return format.format(date)
    }

}
