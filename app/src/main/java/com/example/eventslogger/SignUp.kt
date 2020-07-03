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
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUp : Fragment() {
    private  val RC_SIGN_IN = 1
    private  val callBackManager  = CallbackManager.Factory.create()

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

        button_signUp.setOnClickListener {
            val email = sign_up_username.text.toString()
            val password = sign_up_password.text.toString()
            val signUpSuccess = FireBaseWorker(context).signUp(email,password)
            if(FireBaseWorker(context).currentUser != null){
                Toast.makeText(context, "Sign Up Complete", Toast.LENGTH_SHORT).show()
                updateUI()
            } else{
                Toast.makeText(context, "Sign Up InComplete", Toast.LENGTH_SHORT).show()
            }
        }

        button_google_sign_up.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(R.string.default_web_client_id.toString()).requestEmail().build()
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