package com.ute.co_study.feature.auth.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityWelcomeBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.auth.ui.login.LoginActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.heroTitle, true)

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.registerButton.setOnClickListener {
            Toast.makeText(this, R.string.auth_registration_unavailable, Toast.LENGTH_SHORT).show()
        }
    }
}
