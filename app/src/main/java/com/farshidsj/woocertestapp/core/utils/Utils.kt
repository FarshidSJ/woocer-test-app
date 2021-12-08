package com.farshidsj.woocertestapp.core.utils

import com.google.android.material.textfield.TextInputLayout

object Utils {

    var consumerKey = ""
    var consumerSecret = ""

    fun showInputError(textInputLayout: TextInputLayout, error: String) {
        textInputLayout.isErrorEnabled = true
        textInputLayout.errorIconDrawable = null
        textInputLayout.error = error
    }

    fun hideInputError(textInputLayout: TextInputLayout){
        textInputLayout.isErrorEnabled = false
        textInputLayout.error = ""
    }
}