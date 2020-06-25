package com.example.eventslogger

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_first_screen.*


class FirstScreen : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textViewNewUser.setOnClickListener {
            it.findNavController().navigate(R.id.action_firstScreen_to_signUp)

        }

        button_sign_in.setOnClickListener {
            val email = sign_in_username.text.toString()
            val password = sign_in_password.text.toString()
            val successful = FireBaseWorker(context).signIn(email, password)
            if(successful){
                Toast.makeText(context, "Sign in Complete", Toast.LENGTH_SHORT).show()
                val intent = Intent (activity,MainActivity::class.java)
                requireActivity().startActivity(intent)
                requireActivity().finish()
            } else{
                Toast.makeText(context, "Sign Up InComplete", Toast.LENGTH_SHORT).show()
            }
        }
    }
}