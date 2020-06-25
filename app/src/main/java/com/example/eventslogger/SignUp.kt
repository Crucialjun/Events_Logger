package com.example.eventslogger

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUp : Fragment() {
    private var email : String = ""
    private var password : String = ""

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
            val successful = FireBaseWorker(context).signup(email, password)
            if(successful){
                Toast.makeText(context, "Sign Up Complete", Toast.LENGTH_SHORT).show()
                val intent = Intent (activity,MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            } else{
                Toast.makeText(context, "Sign Up InComplete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}