package com.example.socialmediaclone

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmediaclone.Models.User
import com.example.socialmediaclone.utils.USER_NODE
import com.example.socialmediaclone.utils.USER_PROFILE_FOLDER
import com.example.socialmediaclone.utils.uploadImage
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class SignUpActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    lateinit var user:User
    lateinit var addProfile: CircleImageView
    lateinit var login:TextView

    private val launcher=registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->

        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER){
                if (it!=null){
                    user.image=it
                    addProfile.setImageURI(uri)
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        var signUpBtn=findViewById<Button>(R.id.signUpBtn)
        addProfile=findViewById(R.id.profile_image)
        var name=findViewById<TextInputEditText>(R.id.name)
        var email=findViewById<TextInputEditText>(R.id.email)
        var password=findViewById<TextInputEditText>(R.id.password)


        login=findViewById(R.id.login)

        user= User()

        if (intent.hasExtra("MODE")){
            if (intent.getIntExtra("MODE",-1)==1){
                signUpBtn.text="Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {
                        user=it.toObject<User>()!!
                        if (!user.image.isNullOrEmpty()){
                            Picasso.get().load(user.image).into(addProfile)
                        }
                        name.setText(user.name)
                        email.setText(user.email)
                        password.setText(user.password)
                    }
            }
        }




        val text = "<font color=#FF000000>Already have an account </font> <font color=#1E88E5>Login</font>"
        login.setText(Html.fromHtml(text))

        signUpBtn.setOnClickListener {

            if (intent.hasExtra("MODE")){
                if (intent.getIntExtra("MODE",-1)==1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(
                                Intent(
                                    this@SignUpActivity,
                                    HomeActivity::class.java
                                )
                            )
                            finish()
                        }
                }
            }
            else {
                if (name.text.toString().equals("") or
                    email.text.toString().equals("") or
                    password.text.toString().equals("")
                ) {
                    Toast.makeText(
                        this@SignUpActivity,
                        "Please fill all information ",
                        Toast.LENGTH_LONG
                    ).show()
                } else {

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        email.text.toString(),
                        password.text.toString()
                    ).addOnCompleteListener { result ->

                        if (result.isSuccessful) {
                            user.name = name.text.toString()
                            user.email = email.text.toString()
                            user.password = password.text.toString()

                            Firebase.firestore.collection(USER_NODE)
                                .document(Firebase.auth.currentUser!!.uid).set(user)
                                .addOnSuccessListener {
                                    startActivity(
                                        Intent(
                                            this@SignUpActivity,
                                            HomeActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                result.exception?.localizedMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }
            }
        }

        addProfile.setOnClickListener{
            launcher.launch("image/*")
        }

        login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity,LoginActivity::class.java))
            finish()
        }
    }
}