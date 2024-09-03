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
import com.example.jobstest.MainActivity
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

        // Восстанавливаю состояние полей, если есть сохраненное состояние
        savedInstanceState?.let {
            et1.setText(it.getString("et1_text"))
            et2.setText(it.getString("et2_text"))
            et3.setText(it.getString("et3_text"))
            et4.setText(it.getString("et4_text"))
        }

        setupEditTexts()
        updateButtonState()

        continueButton.setOnClickListener {
            // Выполняем переход на SearchFragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.content, Search())
                .addToBackStack(null)
                .commit()

            // Активируем меню после перехода
            (activity as MainActivity).activateBottomNavigation()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущее состояние полей EditText
        if (::et1.isInitialized || ::et2.isInitialized || ::et3.isInitialized || ::et4.isInitialized ){
            outState.putString("et1_text", et1.text.toString())
            outState.putString("et2_text", et2.text.toString())
            outState.putString("et3_text", et3.text.toString())
            outState.putString("et4_text", et4.text.toString())
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