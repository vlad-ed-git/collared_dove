package com.dev_vlad.collared_doves.utils

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.dev_vlad.collared_doves.R
import com.google.android.material.snackbar.Snackbar


fun View.showSnackBarToUser(
    msgResId: Int,
    isErrorMsg: Boolean = true,
    actionMessage: Int? = null,
    actionToTake: ((View) -> Unit) = {}
): Snackbar {

    val showForTime =
        if (actionMessage == null) Snackbar.LENGTH_LONG else Snackbar.LENGTH_INDEFINITE
    val snackBar = Snackbar.make(this, context.getString(msgResId), showForTime)
    val mainSnackBarTxt =
        snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    val actionTxt =
        snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)

    //set background color
    snackBar.view.setBackgroundColor(
        ContextCompat.getColor(
            this.context,
            R.color.secondary_dark
        )
    )

    //set text color
    if (isErrorMsg) {
        mainSnackBarTxt.setTextColor(ContextCompat.getColor(this.context, R.color.design_default_color_error))
        actionTxt.setTextColor(ContextCompat.getColor(this.context, R.color.design_default_color_error))
    } else {
        mainSnackBarTxt.setTextColor(ContextCompat.getColor(this.context, R.color.white))
        actionTxt.setTextColor(ContextCompat.getColor(this.context, R.color.white))
    }

    //set the font
    ResourcesCompat.getFont(this.context, R.font.lateef)?.let {
        mainSnackBarTxt.typeface = it
        actionTxt.typeface = it
    }

    //set the size
    mainSnackBarTxt.textSize = 16.toFloat()
    actionTxt.textSize = 16.toFloat()

    //display snackbar
    if (actionMessage != null) {
        snackBar.setAction(context.getString(actionMessage)) {
            actionToTake(this)
        }.show()
    } else {
        snackBar.show()
    }
    return snackBar
}