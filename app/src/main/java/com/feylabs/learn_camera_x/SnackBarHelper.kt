package com.feylabs.learn_camera_x

import com.google.android.material.snackbar.Snackbar

import android.content.Intent

import android.app.Activity
import android.graphics.Color
import android.view.View


object SnackBarHelper{
    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen for few seconds */
    fun showSnakbarTypeOne(rootView: View, mMessage: String?) {
        Snackbar.make(rootView, mMessage!!, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen */
    fun showSnakbarTypeTwo(rootView: View, mMessage: String?) {
        Snackbar.make(rootView, mMessage!!, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    /************************************ ShowSnackbar without message, KeepItDisplayedOnScreen, OnClickOfOk restrat the activity */
    fun showSnakbarTypeThree(rootView: View, activity: Activity) {
        Snackbar
            .make(rootView, "NoInternetConnectivity", Snackbar.LENGTH_INDEFINITE)
            .setAction("TryAgain", object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val intent = activity.intent
                    activity.finish()
                    activity.startActivity(intent)
                }
            })
            .setActionTextColor(Color.CYAN)
            .setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                }

                override fun onShown(snackbar: Snackbar) {
                    super.onShown(snackbar)
                }
            })
            .show()
    }

    /************************************ ShowSnackbar with message, KeepItDisplayedOnScreen, OnClickOfOk restrat the activity */
    fun showSnakbarTypeFour(rootView: View, activity: Activity, mMessage: String?) {
        Snackbar
            .make(rootView, mMessage!!, Snackbar.LENGTH_INDEFINITE)
            .setAction("TryAgain") {
                val intent = activity.intent
                activity.finish()
                activity.startActivity(intent)
            }
            .setActionTextColor(Color.CYAN)
            .setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                }

                override fun onShown(snackbar: Snackbar) {
                    super.onShown(snackbar)
                }
            })
            .show()
    }
}