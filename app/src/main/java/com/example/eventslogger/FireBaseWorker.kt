package com.example.eventslogger

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider


class FireBaseWorker(val context : Context?) {
    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    val callBackManager = CallbackManager.Factory.create()
    private var userId : String = ""
    private lateinit var  userReference : DatabaseReference
    var isSuccessful : Boolean = false

    fun signUp(email:String, password : String): Boolean {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    userId = mAuth.currentUser!!.uid
                    userReference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    val userHash = HashMap<String, Any>()
                    userHash["uid"] = userId
                    userReference.updateChildren(userHash).addOnCompleteListener { task ->
                        isSuccessful = if(task.isSuccessful){
                            true
                        }else{
                            Toast.makeText(context,"Database Not Updated",Toast.LENGTH_LONG).show()
                            false
                        }
                    }


                } else {
                    Toast.makeText(context, "Sign up unSuccessful because of ${it.exception}", Toast.LENGTH_LONG).show()
                }
            }

        return isSuccessful

    }

    fun signIn(email:String, password:String): Boolean {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            isSuccessful = it.isSuccessful
        }

        return isSuccessful
    }

    public fun googleSignIn(data: Intent?): Boolean {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

            mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        isSuccessful = true
                    } else {
                        isSuccessful = false
                        Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "onActivityResult: $exception ")
        }

        return isSuccessful


    }

    public fun fbSignIn(){

        LoginManager.getInstance().registerCallback(callBackManager,object : FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                val user = mAuth.currentUser
                isSuccessful = true
            }else{
                Toast.makeText(context, "Facebook Login Unsucceful", Toast.LENGTH_SHORT).show()
            }
        }
    }


}