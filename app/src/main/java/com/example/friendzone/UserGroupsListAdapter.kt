package com.example.friendzone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.security.acl.Group

class SongListAdapter(private var songs: List<Group>):  RecyclerView.Adapter<SongListAdapter.SongListViewHolder>(){

    var onGroupClickListener: ((group: Group) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_group, parent, false)
        return SongListViewHolder(view)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int): Unit {
        val person = songs[position]
        holder.bind(person, position)
    }

//    fun change(shuffledSongs: List<>) {
//
//        val callback = SongDiffCallback(songs, shuffledSongs)
//        val diffResult = DiffUtil.calculateDiff(callback)
//        diffResult.dispatchUpdatesTo(this)
//
//        songs = shuffledSongs
//
//    }

    inner class GroupListViewHolder(itemView: View):  RecyclerView.ViewHolder(itemView) {
        private val displayName = itemView.findViewById<TextView>(R.id.displayName)

        fun bind(song: Song, position: Int) {
            title.text = song.title
            artist.text = song.artist
            songImg.setImageResource(song.smallImageID)

            itemView.setOnClickListener {
                onGroupClickListener?.invoke(song)
            }
        }
    }
}