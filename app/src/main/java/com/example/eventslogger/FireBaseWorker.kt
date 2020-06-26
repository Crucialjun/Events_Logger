package com.example.eventslogger

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


class FireBaseWorker(val context : Context?) {
    public val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userId : String = ""
    private lateinit var  userReference : DatabaseReference

    public fun signUp(email:String, password : String): Boolean {
        var isSuccessful : Boolean = true

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
                            isSuccessful = false
                        }
                    }


                } else {
                    Toast.makeText(context, "Sign up unSuccessful because of ${it.exception}", Toast.LENGTH_LONG).show()
                }
            }

        return isSuccessful

    }

    public fun signIn(email:String, password:String): Boolean {
        var isSuccess : Boolean = true

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            isSuccess = it.isSuccessful
        }

        return isSuccess
    }

    public fun googleSignIn(data: Intent?): Boolean {
        var isSuccess : Boolean = true
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

            mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isSuccess = true
                    } else {
                        isSuccess = false
                        Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "onActivityResult: $exception ")
        }

        return isSuccess


    }

    public fun fbSignIn(){
        FacebookSdk.setApplicationId("321180588905861")
        FacebookSdk.sdkInitialize(context)

        val callBackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callBackManager,object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(context, "Facebook Sign In Okay ", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }




}