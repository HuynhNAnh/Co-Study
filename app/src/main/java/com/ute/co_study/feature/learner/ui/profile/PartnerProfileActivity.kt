package com.ute.co_study.feature.learner.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityPartnerProfileBinding
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.feature.learner.model.SamplePartners
import com.ute.co_study.feature.learner.ui.discovery.levelLabel
import com.ute.co_study.feature.learner.ui.discovery.timeSlotLabel
import com.ute.co_study.feature.learner.ui.exchange.ExchangeRequestActivity
import com.ute.co_study.feature.learner.ui.study.arguments
import com.ute.co_study.feature.learner.ui.study.studyConversation

class PartnerProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val conversation = intent.studyConversation()
        val partner = SamplePartners.find(conversation) ?: return finish()
        val binding = ActivityPartnerProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyAuthInsets(binding.root, binding.statusBarSpace)
        ViewCompat.setAccessibilityHeading(binding.screenTitle, true)
        ViewCompat.setAccessibilityHeading(binding.partnerName, true)

        binding.partnerInitials.text = conversation.initials
        binding.partnerName.text = conversation.title
        binding.partnerBio.text = partner.bio
        binding.partnerTrust.text = getString(R.string.partner_score_out_of_100, partner.trustScore)
        binding.partnerRating.text = partner.rating
        binding.partnerCompleted.text = partner.completedSessions.toString()
        binding.partnerShares.text = getString(R.string.partner_profile_share_value, partner.shares, levelLabel(partner.level))
        binding.partnerWants.text = partner.wants
        val availability = partner.availability.joinToString("\n") { timeSlotLabel(it) }
        binding.partnerMatch.text = getString(R.string.partner_profile_match, partner.matchScore, availability)

        binding.backButton.setOnClickListener { finish() }
        binding.exchangeButton.setOnClickListener {
            startActivity(Intent(this, ExchangeRequestActivity::class.java).putExtras(conversation.arguments()))
        }
        binding.reportButton.setOnClickListener {
            startActivity(Intent(this, ReportPartnerActivity::class.java).putExtras(conversation.arguments()))
        }
    }
}
