package com.makks.bigtalk.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.makks.bigtalk.R

// todo delete
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)
        findViewById<Button>(R.id.click).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}