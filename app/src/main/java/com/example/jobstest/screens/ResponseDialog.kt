package com.example.jobstest.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.jobstest.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ResponseDialog : BottomSheetDialogFragment() {

    lateinit var addCoverLetterTextView: TextView
    lateinit var letterEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_response_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applyButton = view.findViewById<Button>(R.id.apply_button)
        addCoverLetterTextView = view.findViewById<TextView>(R.id.add_cover_letter_text)
        letterEditText = view.findViewById<EditText>(R.id.letter_et)


        // Восстанавливаю состояние
        savedInstanceState?.let {
            val isAddCoverLetterVisible = it.getBoolean("add_cover_letter_visibility", true)
            val letterText = it.getString("letter_text", "")

            addCoverLetterTextView.visibility = if (isAddCoverLetterVisible) View.VISIBLE else View.GONE
            letterEditText.visibility = if (isAddCoverLetterVisible) View.GONE else View.VISIBLE
            letterEditText.setText(letterText)
        }

        applyButton.setOnClickListener {
            dismiss()
        }

        addCoverLetterTextView.setOnClickListener {
            addCoverLetterTextView.visibility = View.GONE
            letterEditText.visibility = View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем состояние TextView и EditText
        outState.putBoolean("add_cover_letter_visibility", addCoverLetterTextView.visibility == View.VISIBLE)
        outState.putString("letter_text", letterEditText.text.toString())
    }


}