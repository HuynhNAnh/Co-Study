package com.ute.co_study.feature.learner.ui

import android.content.res.ColorStateList
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.ute.co_study.R

internal fun AppCompatButton.markCurrentLearnerTab() {
    val activeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.co_study_green))
    setBackgroundResource(R.drawable.bg_auth_notice)
    setTextColor(activeColor)
    TextViewCompat.setCompoundDrawableTintList(this, activeColor)
    isSelected = true
    isClickable = false
    ViewCompat.setStateDescription(this, context.getString(R.string.home_tab_current))
}

internal fun showLearnerFeatureUnavailable(vararg views: View) {
    views.forEach { view ->
        view.setOnClickListener {
            Toast.makeText(view.context, R.string.home_feature_unavailable, Toast.LENGTH_SHORT).show()
        }
    }
}
