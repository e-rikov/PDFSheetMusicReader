package ru.rikov.evgeniy.pdfsheetmusicreader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.rikov.evgeniy.pdfsheetmusicreader.feature.main.MainFragment


class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val fileUri = intent?.data

        if ((savedInstanceState == null) && (fileUri != null)) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(fileUri))
                .commitNow()
        }
    }

}