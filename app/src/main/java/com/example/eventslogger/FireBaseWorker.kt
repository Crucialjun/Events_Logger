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
import kotlin.properties.Delegates


class FireBaseWorker(val context : Context?) {
    val  mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userId : String = ""
    private lateinit var  userReference : DatabaseReference
    var currentUser = mAuth.currentUser
    

    fun signUp(email:String, password : String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {

                    userId = mAuth.currentUser!!.uid
                    userReference =
                        FirebaseDatabase.getInstance().reference.child("Users").child(userId)

                    val userHash = HashMap<String, Any>()
                    userHash["uid"] = userId
                    userReference.updateChildren(userHash).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(context,"Database Not Updated Successfully",Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"Database Not Updated",Toast.LENGTH_LONG).show()

                        }
                    }


                } else {
                    Toast.makeText(context, "Sign up unSuccessful because of ${it.exception}", Toast.LENGTH_LONG).show()
                }
            currentUser = mAuth.currentUser
            }


    }

    fun signIn(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
           // isSuccessful = it.isSuccessful
        }
    }

    public fun googleSignIn(data: Intent?): Boolean {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)

            val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

            mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        //isSuccessful = true
                    } else {
                       // isSuccessful = false
                        Toast.makeText(context, "Google sign in failed:(", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        } catch (exception: Exception) {
            Log.d(ContentValues.TAG, "onActivityResult: $exception ")
        }

        return true


    }



    public fun handleFacebookAccessToken(accessToken: AccessToken?) : Boolean {
        val credential = accessToken?.token?.let { FacebookAuthProvider.getCredential(it) }
        val auth = FirebaseAuth.getInstance()
        if (credential != null) {
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (it.isSuccessful) {
                    //isSuccessful = true
                } else {
                    Toast.makeText(context, "Sign Up Complete: Error is ${it.result}", Toast.LENGTH_SHORT).show()
                    //isSuccessful = false
                }
            }
        }else{
            Toast.makeText(context, "Credential is null", Toast.LENGTH_SHORT).show()
        }
        return false
    }


}