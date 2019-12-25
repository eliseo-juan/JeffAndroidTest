package dev.eliseo.jeff

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dev.eliseo.jeff.util.ConnectivityLiveData

/**
 * Created by eliseo on 24/12/19.
 */
class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        connectivityLiveData = ConnectivityLiveData(context)
    }

    companion object {

        private var instance: App? = null
        lateinit var connectivityLiveData: ConnectivityLiveData

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }

        val context: Context
            get() = instance!!.applicationContext
    }
}