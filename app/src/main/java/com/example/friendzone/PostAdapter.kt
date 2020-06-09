package com.example.friendzone

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.friendzone.manager.ApiManager
import com.example.friendzone.manager.PostManager
import com.example.friendzone.model.Post
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class PostAdapter(private val initialListOfPosts: List<UploadManager>, private val application: FriendZoneApp): RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private lateinit var parent: Context
    private var listOfPosts: List<UploadManager> = initialListOfPosts.toList()
    private lateinit var reactionAdapter: ReactionAdapter

    private var mediaPlayer: MediaPlayer? = null

    private var audio : String? = null

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
        private val ivPlay = itemView.findViewById<ImageView>(R.id.ivPlay)
        private val tvDesc = itemView.findViewById<TextView>(R.id.tvDesc)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(post: UploadManager) {
            var apiManager = (application as FriendZoneApp).apiManager
            tvUsername.text = post.userName
            Picasso.get().load(post.imageUrl).into(ivPostImage)
            tvTimestamp.text = convertLongToTime(post.timeStamp!!)
            tvDesc.text = "feeling ${post.postDesc}"
            val hasAudio = post.audioUrl != ""
            val reactions = arrayOf(HAPPY, LAUGH, SAD, ANGRY, SURPRISE).toMutableList()
            var adapter = ArrayAdapter(parent, android.R.layout.simple_list_item_1, reactions)


            itemView.setOnClickListener {
                onPostClickListener?.invoke(post)
            }

            if (!hasAudio) {
                ivPlay.visibility = View.GONE
            } else {
                ivPlay.visibility = View.VISIBLE
            }

            ivPostImage.setOnClickListener {
                if (post.audioUrl != "") {
                    try {
                        if (mediaPlayer == null) {
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(post.audioUrl)
                                prepare()
                                start()
                            }
                            audio = post.audioUrl
                        } else if (mediaPlayer != null && audio != post.audioUrl) {
                            mediaPlayer!!.stop()
                            mediaPlayer = MediaPlayer().apply {
                                setDataSource(post.audioUrl)
                                prepare()
                                start()
                            }
                            audio = post.audioUrl
                        } else {
                            mediaPlayer!!.stop()
                            mediaPlayer = null
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            ivPostImage.setOnTouchListener { v, event ->
                touchX = event.rawX.toInt()
                touchY = event.rawY.toInt()

                false
            }

            ivPostImage.setOnLongClickListener {
                createPopupWindow(position, apiManager, post).showAtLocation(ivPostImage, 0, touchX!!, touchY!!)

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

    private fun createPopupWindow(itemPosition: Int, apiManager: ApiManager, post: UploadManager): PopupWindow {
        var popup: PopupWindow? = null

        val popupMenuView = LayoutInflater.from(parent).inflate(R.layout.emoji_selection_popup, null).apply {
            findViewById<TextView>(R.id.btnAngry).setOnClickListener {
                apiManager.addReaction(application.auth.currentUser!!.displayName.toString(), "\uD83D\uDE20", post.shareTo.toString(), "${post.timeStamp}${post.userID}", {popup?.dismiss()},{})
            }
            findViewById<TextView>(R.id.btnSad).setOnClickListener {
                apiManager.addReaction(application.auth.currentUser!!.displayName.toString(), "\uD83D\uDE22", post.shareTo.toString(), "${post.timeStamp}${post.userID}", {popup?.dismiss()},{})
            }
            findViewById<TextView>(R.id.btnHappy).setOnClickListener {
                apiManager.addReaction(application.auth.currentUser!!.displayName.toString(), "\uD83D\uDE03", post.shareTo.toString(), "${post.timeStamp}${post.userID}", {popup?.dismiss()},{})
            }
        }

        popup = PopupWindow(popupMenuView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        return popup

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

