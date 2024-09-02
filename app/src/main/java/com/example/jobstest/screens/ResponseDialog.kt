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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_response_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val applyButton = view.findViewById<Button>(R.id.apply_button)
        val addCoverLetterTextView = view.findViewById<TextView>(R.id.add_cover_letter_text)
        val letterEditText = view.findViewById<EditText>(R.id.letter_et)

        applyButton.setOnClickListener {
            dismiss()
        }

        addCoverLetterTextView.setOnClickListener {
            addCoverLetterTextView.visibility = View.GONE
            letterEditText.visibility = View.VISIBLE
        }
    }


}