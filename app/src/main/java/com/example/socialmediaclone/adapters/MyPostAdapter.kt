package com.example.socialmediaclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmediaclone.Models.Post
import com.example.socialmediaclone.databinding.MyPostDesignBinding
import com.squareup.picasso.Picasso

class MyPostAdapter(var context:Context,var postList:ArrayList<Post>) :
    RecyclerView.Adapter<MyPostAdapter.ViewHolder>(){

    inner class ViewHolder(var binding:MyPostDesignBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding=MyPostDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.postImage
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.postImage)
    }
}