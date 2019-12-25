package dev.eliseo.jeff.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.eliseo.jeff.R
import dev.eliseo.jeff.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}
