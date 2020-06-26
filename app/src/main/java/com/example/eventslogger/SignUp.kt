package com.example.eventslogger

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUp : Fragment() {
    private var email : String = ""
    private var password : String = ""
    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_sign_up, container, false)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_signUp.setOnClickListener {
            val email = sign_up_username.text.toString()
            val password = sign_up_password.text.toString()
            val successful = FireBaseWorker(context).signUp(email, password)
            if(successful){
                Toast.makeText(context, "Sign Up Complete", Toast.LENGTH_SHORT).show()
                val intent = Intent (activity,MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
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

        button_fb_sign_up.setOnClickListener {
            FireBaseWorker(context).fbSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            FireBaseWorker(context).googleSignIn(data)
            Toast.makeText(context, "Sign Up Complete", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }else{
            Toast.makeText(context, "The request code recieved is $requestCode", Toast.LENGTH_SHORT).show()
        }
    }
}