package com.example.jobstest.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jobstest.R


class LoginSecond : Fragment() {

    private lateinit var et1: EditText
    private lateinit var et2: EditText
    private lateinit var et3: EditText
    private lateinit var et4: EditText
    private lateinit var continueButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email = arguments?.getString("email") ?: ""
        val sendMailTextView = view.findViewById<TextView>(R.id.send_mail)
        sendMailTextView.text = getString(R.string.send_code, email)

        et1 = view.findViewById(R.id.et1)
        et2 = view.findViewById(R.id.et2)
        et3 = view.findViewById(R.id.et3)
        et4 = view.findViewById(R.id.et4)
        continueButton = view.findViewById(R.id.continue_bt)

        setupEditTexts()
        updateButtonState()

        continueButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginSecondFragment_to_searchFragment)
        }
    }

    private fun setupEditTexts() {
        et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) et2.requestFocus()
                updateButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) et3.requestFocus()
                else if (s.isNullOrEmpty()) et1.requestFocus()
                updateButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) et4.requestFocus()
                else if (s.isNullOrEmpty()) et2.requestFocus()
                updateButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        et4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isEmpty() == true) et3.requestFocus()
                updateButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateButtonState() {
        continueButton.isEnabled = et1.text.isNotEmpty() &&
                et2.text.isNotEmpty() &&
                et3.text.isNotEmpty() &&
                et4.text.isNotEmpty()
    }
}