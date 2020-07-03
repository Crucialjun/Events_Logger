package com.example.eventslogger

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUp : Fragment() {
    private  val RC_SIGN_IN = 1
    private  val callBackManager  = CallbackManager.Factory.create()
    private val  mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private var userId : String = ""
    private lateinit var  userReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_fb_sign_up.fragment = this

        val fireBaseWorker = FireBaseWorker(context).isSuccess

        button_signUp.setOnClickListener {
            val email = sign_up_username.text.toString()
            val password = sign_up_password.text.toString()
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    userId = mAuth.currentUser!!.uid
                    userReference = FirebaseDatabase
                        .getInstance()
                        .reference
                        .child("Users")
                        .child(userId)

                    val userHash = HashMap<String, Any>()
                    userHash["uid"] = userId
                    userHash["email"] = mAuth.currentUser!!.email.toString()
                    userReference.updateChildren(userHash).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            updateUI()
                            Toast.makeText(context,"Database Not Updated Successfully",Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"Database Not Updated",Toast.LENGTH_LONG).show()

                        }
                    }

                } else {
                    Toast.makeText(context, "Sign up unSuccessful because of ${it.exception}", Toast.LENGTH_LONG).show()
                }

            }
        }

        button_google_sign_up.setOnClickListener {
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(R.string.default_web_client_id.toString()).requestEmail().build()
            val mGoogleSignInClient = GoogleSignIn.getClient(requireContext(),gso)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent,RC_SIGN_IN)
    }



        button_fb_sign_up.setPermissions("email","public_profile")
        button_fb_sign_up.registerCallback(callBackManager,object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Toast.makeText(context, "Login result : $result", Toast.LENGTH_SHORT).show()
                val success : Boolean = FireBaseWorker(context).handleFacebookAccessToken(result!!.accessToken)
                if(success){
                    updateUI()
                }
            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(context, "Login result : ${error.toString()}", Toast.LENGTH_SHORT).show()

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callBackManager.onActivityResult(requestCode,resultCode,data)

        if (requestCode == RC_SIGN_IN) {
            FireBaseWorker(context).googleSignIn(data)
            updateUI()
        }else{

        }

    }

    private fun updateUI(){
        val intent = Intent(activity, MainActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

}