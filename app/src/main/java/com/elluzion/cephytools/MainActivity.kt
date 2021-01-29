package com.elluzion.cephytools

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.DataOutputStream
import java.io.IOException
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDevice()
        setupLabels()
        setOnClickListeners()
    }
    private fun setupLabels() {
        this.main_header_sub.text = String.format(getString(R.string.version), BuildConfig.VERSION_CODE)
    }
    private fun setOnClickListeners () {
        app_action_airemap.setOnClickListener {
            val intent = Intent(this, AIButtonRemapperActivity::class.java).apply {
            }
            startActivity(intent)

        }
    }
    private fun checkDevice() {
        if (Build.DEVICE != "cepheus") {
            Toast.makeText(this, "Error: Failed device check: Your device is not supported!", Toast.LENGTH_LONG).show()
            this.finish()
        }
    }
}