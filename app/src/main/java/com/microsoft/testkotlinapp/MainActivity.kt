package com.microsoft.testkotlinapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.CustomProperties
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.microsoft.appcenter.distribute.Distribute
import com.microsoft.appcenter.push.Push
import com.microsoft.appcenter.utils.async.AppCenterConsumer


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MyApp"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Push.setListener(MyPushListener())
        AppCenter.start(application, BuildConfig.APP_SECRET,
            Analytics::class.java, Crashes::class.java, Distribute::class.java, Push::class.java)

        AppCenter.setLogLevel(Log.VERBOSE)

        val properties = hashMapOf("Category" to "Music", "FileName" to "favorite.avi")
        Analytics.trackEvent("App startup", properties)

        AppCenter.getInstallId()

        val customProperties = CustomProperties()
        customProperties.clear("score")
        AppCenter.setCustomProperties(customProperties)


        // Use 20MB for storage.
        AppCenter.setMaxStorageSize(20 * 1024 * 1024).thenAccept(object : AppCenterConsumer<Boolean> {
            override fun accept(success: Boolean) {
                // The success parameter is false when the size cannot be honored.
            }
        })

        AppCenter.start(application, "{Your App Secret}", Analytics::class.java)

        val future = AppCenter.setEnabled(true)
        future.thenAccept(object : AppCenterConsumer<Void> {
            override fun accept(t: Void?) {
                // do something with result, this is called back in U.I. thread.
            }

        })


        AppCenter.isEnabled().thenAccept(object : AppCenterConsumer<Boolean> {
            override fun accept(enabled: Boolean?) {
                Log.d(TAG, "AppCenter.isEnabled=$enabled")
            }
        })

        val enabled = AppCenter.isEnabled().get()
        Log.d(TAG, enabled.toString())

    }
}
