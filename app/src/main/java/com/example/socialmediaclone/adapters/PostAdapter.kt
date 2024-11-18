package com.example.socialmediaclone.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmediaclone.Models.Post
import com.example.socialmediaclone.Models.User
import com.example.socialmediaclone.R
import com.example.socialmediaclone.databinding.PostRvBinding
import com.example.socialmediaclone.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.io.FileOutputStream

class PostAdapter(var context:Context,var postList:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.MyHolder>() {
    inner class MyHolder(var binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        try {
            Firebase.firestore.collection(USER_NODE).document(postList[position].uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()

                    Glide.with(context).load(user!!.image).placeholder(R.drawable.profile)
                        .into(holder.binding.profileImage)
                    holder.binding.name.text = user.name
                }
        } catch (e: Exception) {

        }

        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.loading)
            .into(holder.binding.postImage)

        try {
            val text: String = TimeAgo.using(postList.get(position).time.toLong())
            holder.binding.time.text = text

        } catch (e: Exception) {
            holder.binding.time.text = ""
        }

        holder.binding.caption.text = postList.get(position).caption


        var isLiked = false

        holder.binding.likebtn.setOnClickListener {
            if (isLiked) {
                // Change to empty heart icon
                holder.binding.likebtn.setImageResource(R.drawable.empty_heart)
                isLiked = false
                // Remove like or perform any other action
            } else {
                // Change to filled heart icon
                holder.binding.likebtn.setImageResource(R.drawable.filled_heart)
                isLiked = true
                // Add like or perform any other action
            }
        }
        holder.binding.messagebtn.setOnClickListener {
            var i = Intent(android.content.Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, postList.get(position).postUrl)
            context.startActivity(i)

        }
        holder.binding.savebtn.setOnClickListener {
            val drawable = holder.binding.postImage.drawable
            if (drawable is BitmapDrawable) {
                val bitmap = drawable.bitmap

                // Save the bitmap to internal storage
                val fileName = "image_${System.currentTimeMillis()}.jpg"
                val outputStream: FileOutputStream
                try {
                    outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.close()

                    // Notify the user that the image has been saved
                    Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}