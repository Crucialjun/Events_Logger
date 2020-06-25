package com.example.eventslogger

import android.content.Context
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseWorker(val context : Context?) {
    private val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userId : String = ""
    lateinit var  userReference : DatabaseReference

    public fun signup(email:String,password : String){
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful){
                userId = mAuth.currentUser!!.uid
                userReference = FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                val userHash = HashMap<String,Any>()
                userHash["uid"] = userId

                Toast.makeText(context,"Sign Up Succesfull",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(context,"Sign up unSuccesfull",Toast.LENGTH_LONG).show()
            }
        }
    }



}