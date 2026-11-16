package com.ute.co_study.feature.auth.ui.welcome

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityWelcomeBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)

        binding.loginButton.setOnClickListener {
            Toast.makeText(this, R.string.auth_login_unavailable, Toast.LENGTH_SHORT).show()
        }
        binding.registerButton.setOnClickListener {
            Toast.makeText(this, R.string.auth_registration_unavailable, Toast.LENGTH_SHORT).show()
        }
    }
}
