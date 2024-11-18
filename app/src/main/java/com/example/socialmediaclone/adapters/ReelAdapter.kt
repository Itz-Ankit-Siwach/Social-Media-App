package com.example.socialmediaclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaclone.Models.Reel
import com.example.socialmediaclone.R
import com.example.socialmediaclone.databinding.ReelDesignBinding
import com.squareup.picasso.Picasso

class ReelAdapter(var comtect:Context,var reelList: ArrayList<Reel>):RecyclerView.Adapter<ReelAdapter.viewHolder>() {

    inner class viewHolder(var binding:ReelDesignBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var binding=ReelDesignBinding.inflate(LayoutInflater.from(comtect),parent,false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Picasso.get().load(reelList.get(position).profileLink).placeholder(R.drawable.profile).into(holder.binding.profileImage)
        holder.binding.caption.setText(reelList.get(position).caption)
        holder.binding.videoView.setVideoPath(reelList.get(position).reelUrl)
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.progressBar.visibility= View.GONE
            holder.binding.videoView.start()
        }
    }
}