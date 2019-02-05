package com.microsoft.testkotlinapp

import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import com.microsoft.appcenter.push.PushListener
import com.microsoft.appcenter.push.PushNotification

class MyPushListener : PushListener {

    override fun onPushNotificationReceived(activity: Activity, pushNotification: PushNotification) {

        /* The following notification properties are available. */
        val title = pushNotification.getTitle()
        val message = pushNotification.getMessage()
        val customData = pushNotification.getCustomData()

        /*
         * Message and title cannot be read from a background notification object.
         * Message being a mandatory field, you can use that to check foreground vs background.
         */
        if (message != null) {

            /* Display an alert for foreground push. */
            val dialog = AlertDialog.Builder(activity)
            if (title != null) {
                dialog.setTitle(title)
            }
            dialog.setMessage(message)
            if (!customData.isEmpty()) {
                dialog.setMessage(message + "\n" + customData)
            }
            dialog.setPositiveButton(android.R.string.ok, null)
            dialog.show()
        } else {

            /* Display a toast when a background push is clicked. */
            Toast.makeText(
                activity,
                String.format(activity.getString(R.string.push_toast), customData),
                Toast.LENGTH_LONG
            ).show() // For example R.string.push_toast would be "Push clicked with data=%1s"
        }
    }
}