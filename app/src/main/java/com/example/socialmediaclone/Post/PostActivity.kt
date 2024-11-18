package com.example.socialmediaclone.Post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.example.socialmediaclone.HomeActivity
import com.example.socialmediaclone.Models.Post
import com.example.socialmediaclone.Models.User
import com.example.socialmediaclone.R
import com.example.socialmediaclone.databinding.ActivityPostBinding
import com.example.socialmediaclone.utils.POST
import com.example.socialmediaclone.utils.POST_FOLDER
import com.example.socialmediaclone.utils.USER_NODE
import com.example.socialmediaclone.utils.USER_PROFILE_FOLDER
import com.example.socialmediaclone.utils.uploadImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class PostActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl:String?=null
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->

        uri?.let{
            uploadImage(uri, POST_FOLDER){
                url->
                if (url!=null){
                    binding.selectimage.setImageURI(uri)
                    imageUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }

        binding.selectimage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this@PostActivity,HomeActivity::class.java))
            finish()
        }

        binding.postbtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get()
                .addOnSuccessListener {
                    var user = it.toObject<User>()!!
                    val post: Post = Post(
                        postUrl = imageUrl!!,
                        caption = binding.cap.text.toString(),
                        uid =Firebase.auth.currentUser!!.uid,
                        time = System.currentTimeMillis().toString()
                    )

                    Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                            .set(post)
                            .addOnSuccessListener {
                                startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                                finish()
                            }
                    }
                }
        }
    }
}