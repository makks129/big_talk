package com.makks.bigtalk.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.makks.bigtalk.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, QuestionsFragment.newInstance())
                    .commitNow()
        }
    }

}
