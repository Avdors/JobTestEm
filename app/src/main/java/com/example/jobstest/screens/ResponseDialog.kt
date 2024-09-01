package com.example.jobstest.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        applyButton.setOnClickListener {
            dismiss()
        }
    }


}