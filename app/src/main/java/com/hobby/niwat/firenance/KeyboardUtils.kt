package com.hobby.niwat.firenance

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class KeyboardUtils {
    companion object {
        fun showKeyboard(context: Context?, view: View) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}