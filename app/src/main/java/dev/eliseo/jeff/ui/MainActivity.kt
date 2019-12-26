package dev.eliseo.jeff.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.TypedValue
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import dev.eliseo.jeff.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        negotiateDeviceDarkMode()
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
