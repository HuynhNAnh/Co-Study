package com.ute.co_study.feature.moderation.ui.report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.databinding.ActivityRequestMoreInfoBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets

class RequestMoreInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRequestMoreInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)

        binding.backButton.setOnClickListener { finish() }
        binding.backEvidenceButton.setOnClickListener { finish() }

        binding.sendRequestButton.setOnClickListener {
            val intent = Intent(this, InfoRequestedActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            }
            startActivity(intent)
            finish()
        }
    }
}
