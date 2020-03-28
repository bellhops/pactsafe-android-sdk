package com.pactsafe.demo

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText

fun EditText.textChangedListener(textChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            textChanged.invoke(p0.toString())
        }
    })
}

fun EditText.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
fun EditText.isValidPassword() = this.text.length >= 4