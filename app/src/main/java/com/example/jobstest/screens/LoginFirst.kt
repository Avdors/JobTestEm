package com.example.jobstest.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.jobstest.R


class LoginFirst : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var continueButton: Button
    private lateinit var clearImageView: ImageView
    private lateinit var errorEmailTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailEditText = view.findViewById(R.id.email_et)
        continueButton = view.findViewById(R.id.email_continue_bt)
        clearImageView = view.findViewById(R.id.email_clear_image)
        errorEmailTextView = view.findViewById(R.id.error_email)

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                clearImageView.visibility = if (email.isNotEmpty()) View.VISIBLE else View.GONE
                continueButton.isEnabled = email.isNotEmpty()

                if (email.isNotEmpty()) {
                    emailEditText.background = context?.getDrawable(R.drawable.back_grey2_corner8)
                    errorEmailTextView.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        clearImageView.setOnClickListener {
            emailEditText.text.clear()
        }

        continueButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if (isValidEmail(email)) {
                // Открыть новый фрагмент для ввода пароля
                // openNextFragment()
            } else {
                emailEditText.background = context?.getDrawable(R.drawable.error_background)
                errorEmailTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun openNextFragment() {
        // Здесь открываем новый фрагмент с вводом пароля

    }
}