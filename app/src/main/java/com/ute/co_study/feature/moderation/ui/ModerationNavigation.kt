package com.ute.co_study.feature.moderation.ui

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.ute.co_study.R
import com.ute.co_study.databinding.ViewModerationNavigationBinding
import com.ute.co_study.feature.moderation.ui.history.ModerationHistoryActivity
import com.ute.co_study.feature.moderation.ui.overview.ModerationOverviewActivity
import com.ute.co_study.feature.moderation.ui.profile.ModeratorProfileActivity
import com.ute.co_study.feature.moderation.ui.report.ReportListActivity

internal fun setupModerationNavigation(activity: AppCompatActivity, binding: ViewModerationNavigationBinding) {
    val tabs = listOf(
        binding.overviewTab to ModerationOverviewActivity::class.java,
        binding.reportsTab to ReportListActivity::class.java,
        binding.historyTab to ModerationHistoryActivity::class.java,
        binding.accountTab to ModeratorProfileActivity::class.java
    )
    tabs.forEach { (button, destination) ->
        if (destination.isInstance(activity)) {
            val color = ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.co_study_green))
            button.setBackgroundResource(R.drawable.bg_auth_notice)
            button.setTextColor(color)
            TextViewCompat.setCompoundDrawableTintList(button, color)
            button.isSelected = true
            button.isClickable = false
            ViewCompat.setStateDescription(button, activity.getString(R.string.home_tab_current))
        } else {
            button.setOnClickListener {
                activity.startActivity(Intent(activity, destination)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                if (activity !is ModerationOverviewActivity) activity.finish()
            }
        }
    }
}
