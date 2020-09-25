package com.h4rz.remoteconfigdemo.ui.activities

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.h4rz.remoteconfigdemo.R
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : BaseActivity() {

    private lateinit var myContext: Context
    private lateinit var remoteConfig: FirebaseRemoteConfig

    companion object {

        private const val TAG = "MainActivity"

        // Remote Config keys
        private const val CONFIG_KEY_CHANGE_TEXT = "config_key_change_text"
        private const val CONFIG_KEY_IS_CAPS = "config_key_is_caps"
        private const val CONFIG_KEY_CHANGE_COLOR = "config_key_change_color"
        private const val CONFIG_KEY_CHANGE_IMAGE = "config_key_change_image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeVariables()
        configureRemoteConfig()
        setupListeners()
        fetchFromRemoteConfig()
    }

    private fun setupListeners() {
        btnFetch.setOnClickListener {
            fetchFromRemoteConfig()
        }
    }

    private fun fetchFromRemoteConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val results = task.result
                Timber.d("Config Results - $results")
                showToast("Fetch From Remote Config - Success")
            } else
                showToast("Fetch From Remote Config - Failure")
            updateViews()
        }
    }

    private fun configureRemoteConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            // Not recommended while app is in production. Check #Throttling firebase.
            // Fetch config after each #minimumFetchIntervalInSeconds from the previous fetch.
            minimumFetchIntervalInSeconds = 60 // interval duration is in seconds
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Setting default values
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    private fun updateViews() {
        val newText = remoteConfig[CONFIG_KEY_CHANGE_TEXT].asString()
        val isAllCaps = remoteConfig[CONFIG_KEY_IS_CAPS].asBoolean()
        val newColor = remoteConfig[CONFIG_KEY_CHANGE_COLOR].asString()
        val newImage = remoteConfig[CONFIG_KEY_CHANGE_IMAGE].asString()

        tv1?.text = newText
        tv2?.isAllCaps = isAllCaps
        tv3?.setTextColor(Color.parseColor(newColor))
        if (newImage.isNotEmpty())
            Glide.with(myContext).load(newImage).into(ivLogo)
    }

    private fun initializeVariables() {
        myContext = this
    }

    private fun showToast(message: String) {
        Toast.makeText(myContext, message, Toast.LENGTH_SHORT).show()
    }
}