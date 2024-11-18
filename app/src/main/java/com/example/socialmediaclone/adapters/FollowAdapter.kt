package com.example.socialmediaclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaclone.Models.User
import com.example.socialmediaclone.R
import com.example.socialmediaclone.databinding.FollowRvBinding

class FollowAdapter (var context: Context,var followList:ArrayList<User>): RecyclerView.Adapter<FollowAdapter.ViewHolder>(){

    inner class ViewHolder(var binding:FollowRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=FollowRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return followList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(followList.get(position).image).placeholder(R.drawable.profile).into(holder.binding.imageView)

        holder.binding.name.text=followList.get(position).name
    }
}