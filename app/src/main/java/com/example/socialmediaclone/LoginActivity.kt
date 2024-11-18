package com.example.socialmediaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.socialmediaclone.Models.User
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var email=findViewById<TextInputEditText>(R.id.email)
        var password=findViewById<TextInputEditText>(R.id.email)
        var loginBtn=findViewById<Button>(R.id.loginBtn)
        var createAccountBtn=findViewById<Button>(R.id.createAccountBtn)

        loginBtn.setOnClickListener {
            if (email.text.toString().equals("") or
                password.text.toString().equals("")){
                Toast.makeText(this@LoginActivity,"Please Fill the details ",Toast.LENGTH_LONG).show()
            }
            else{
                var user=User(email.text.toString(), password.text.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this@LoginActivity,it.exception?.localizedMessage,Toast.LENGTH_LONG).show()
                        }
                    }
            }
            createAccountBtn.setOnClickListener {
                startActivity(Intent(this@LoginActivity,SignUpActivity::class.java))
            }
        }
    }
}