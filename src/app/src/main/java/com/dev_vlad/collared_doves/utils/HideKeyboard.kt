package com.dev_vlad.collared_doves.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyBoard(context: Context, v: View) {
    val imm = context.getSystemService(InputMethodManager::class.java) as InputMethodManager?
    imm?.hideSoftInputFromWindow(v.windowToken, 0)
}