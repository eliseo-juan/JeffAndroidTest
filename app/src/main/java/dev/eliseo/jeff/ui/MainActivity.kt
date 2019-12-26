package dev.eliseo.jeff.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dev.eliseo.jeff.App
import dev.eliseo.jeff.R
import dev.eliseo.jeff.util.ConnectivityState


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        setupConnectivityManager()
        negotiateDeviceDarkMode()
    }

    private fun setupConnectivityManager() {

        App.connectivityLiveData.observe(this, Observer { connectivityState ->
            when (connectivityState) {
                ConnectivityState.Connected -> hideConnectionIssue()
                ConnectivityState.Disconnected -> showConnectionIssue()
                null -> hideConnectionIssue()
            }
        })
    }

    private fun showConnectionIssue() {
        //TODO Not implemented
    }
    private fun hideConnectionIssue() {
        //TODO Not implemented
    }

    private fun negotiateDeviceDarkMode() {
        val isNightMode = this@MainActivity.resources.configuration.uiMode
            .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val typedValue = TypedValue()

        this@MainActivity.window.addFlags(
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        )

        if (isNightMode) {
            this@MainActivity.theme
                .resolveAttribute(android.R.attr.colorBackground, typedValue, true)
            window.statusBarColor = typedValue.data
        } else {
            this@MainActivity.theme
                .resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
            window.statusBarColor = typedValue.data
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
