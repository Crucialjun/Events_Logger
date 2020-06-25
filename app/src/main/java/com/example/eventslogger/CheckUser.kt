package com.example.eventslogger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class CheckUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_user)

        val userLoggedIn = FireBaseWorker(this).mAuth.currentUser


        if(userLoggedIn != null ){
            val intent = Intent (this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent (this,OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}