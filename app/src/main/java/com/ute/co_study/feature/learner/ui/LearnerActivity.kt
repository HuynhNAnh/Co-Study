package com.ute.co_study.feature.learner.ui

import com.ute.co_study.R
import com.ute.co_study.feature.learner.ui.discovery.DiscoveryActivity
import com.ute.co_study.feature.learner.ui.home.HomeActivity
import com.ute.co_study.feature.learner.ui.study.MessagesActivity
import com.ute.co_study.feature.learner.ui.study.ScheduleActivity
import com.ute.co_study.feature.learner.ui.profile.LearnerProfileActivity
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.ui.WorkflowActivity

abstract class LearnerActivity(layout: Int) : WorkflowActivity(layout, DemoRole.LEARNER) {
    protected fun learnerTabs(current: Int) = tabs(current, listOf(
        Triple(getString(R.string.home_tab_home), R.drawable.ic_home) { returnTo(HomeActivity::class.java) },
        Triple(getString(R.string.home_tab_match), R.drawable.ic_study_partners) { returnTo(DiscoveryActivity::class.java) },
        Triple(getString(R.string.home_tab_messages), R.drawable.ic_message) { returnTo(MessagesActivity::class.java) },
        Triple(getString(R.string.home_tab_schedule), R.drawable.ic_study_calendar) { returnTo(ScheduleActivity::class.java) },
        Triple(getString(R.string.home_tab_profile), R.drawable.ic_person) { returnTo(LearnerProfileActivity::class.java) }
    ))
}
