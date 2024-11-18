package com.example.socialmediaclone.Post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaclone.HomeActivity
import com.example.socialmediaclone.Models.Reel
import com.example.socialmediaclone.Models.User
import com.example.socialmediaclone.R
import com.example.socialmediaclone.databinding.ActivityReelsBinding
import com.example.socialmediaclone.utils.POST_FOLDER
import com.example.socialmediaclone.utils.REEL
import com.example.socialmediaclone.utils.REEL_FOLDER
import com.example.socialmediaclone.utils.USER_NODE
import com.example.socialmediaclone.utils.uploadImage
import com.example.socialmediaclone.utils.uploadVideo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ReelsActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    var videoUrl:String?=null
    lateinit var progressDialog:ProgressDialog
    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
            uri->

        uri?.let{
            uploadVideo(uri, REEL_FOLDER,progressDialog){
                    url->
                if (url!=null){

                    videoUrl=url
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        progressDialog=ProgressDialog(this)

        binding.selectbtn.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
            finish()
        }
        binding.postbtn.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
                var user:User=it.toObject<User>()!!

                val reel: Reel = Reel(videoUrl!!,binding.cap.text.toString(),user.image!!)

                Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                    Firebase.firestore.collection(Firebase.auth.currentUser!!.uid+ REEL).document().set(reel).addOnSuccessListener {
                        startActivity(Intent(this@ReelsActivity,HomeActivity::class.java))
                        finish()
                    }
                }

            }

        }

    }
}