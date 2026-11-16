package com.ute.co_study.feature.moderation.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ViewModerationNavigationBinding
import com.ute.co_study.feature.moderation.ui.history.ModerationHistoryActivity
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.feature.moderation.ui.profile.ModeratorProfileActivity
import com.ute.co_study.feature.moderation.ui.report.ReportListActivity

internal fun AppCompatButton.markCurrentModTab() {
    val activeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.co_study_green))
    setBackgroundResource(R.drawable.bg_auth_notice)
    setTextColor(activeColor)
    TextViewCompat.setCompoundDrawableTintList(this, activeColor)
    isSelected = true
    isClickable = false
    ViewCompat.setStateDescription(this, context.getString(R.string.home_tab_current))
}

internal fun setupModerationNavigation(activity: AppCompatActivity, binding: ViewModerationNavigationBinding) {
    binding.overviewTab.setOnClickListener {
        if (activity !is ModerationOverviewActivity) {
            val intent = Intent(activity, ModerationOverviewActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    binding.reportsTab.setOnClickListener {
        if (activity !is ReportListActivity) {
            val intent = Intent(activity, ReportListActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    binding.historyTab.setOnClickListener {
        if (activity !is ModerationHistoryActivity) {
            val intent = Intent(activity, ModerationHistoryActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            activity.startActivity(intent)
            activity.finish()
        }
    }
    binding.accountTab.setOnClickListener {
        if (activity !is ModeratorProfileActivity) {
            val intent = Intent(activity, ModeratorProfileActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}
