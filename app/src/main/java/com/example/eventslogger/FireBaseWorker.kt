package com.example.eventslogger

import android.content.Context
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FireBaseWorker(val context : Context?) {
    public val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userId : String = ""
    lateinit var  userReference : DatabaseReference

    public fun signup(email:String,password : String): Boolean {
        var isSuccesful : Boolean = true

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    userId = mAuth.currentUser!!.uid
                    userReference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    val userHash = HashMap<String, Any>()
                    userHash["uid"] = userId
                    userReference.updateChildren(userHash).addOnCompleteListener { task ->
                        if(task.isSuccessful){

                        }else{
                            Toast.makeText(context,"Database Not Updated",Toast.LENGTH_LONG).show()
                            isSuccesful = false
                        }
                    }


                } else {
                    Toast.makeText(context, "Sign up unSuccesfull because of ${it.exception}", Toast.LENGTH_LONG).show()
                }
            }

        return isSuccesful

    }

    public fun signIn(email:String, password:String): Boolean {
        var isSuccesful : Boolean = true

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            isSuccesful = it.isSuccessful
        }

        return isSuccesful
    }

    public fun googleSignIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken()
    }




}