package com.ute.co_study.feature.admin.ui

import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback

import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.ute.co_study.R
import com.ute.co_study.databinding.ActivityAdminPrototypeBinding
import com.ute.co_study.feature.admin.model.AdminAction
import com.ute.co_study.feature.admin.model.AdminBlock
import com.ute.co_study.feature.admin.model.AdminScreen
import com.ute.co_study.feature.admin.model.AdminScreenCatalog
import com.ute.co_study.feature.admin.viewmodel.AdminPrototypeViewModel
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.shared.data.DemoRole
import com.ute.co_study.shared.data.DemoSession

/** Admin actor UI for the Co-Study prototype screens A00–A37. */
class AdminPrototypeActivity : AppCompatActivity() {
    private val bg by lazy { getColor(R.color.costudy_background) }
    private val ink by lazy { getColor(R.color.costudy_text) }
    private val muted by lazy { getColor(R.color.costudy_secondary) }
    private val green by lazy { getColor(R.color.costudy_positive) }
    private val mint by lazy { getColor(R.color.costudy_accent) }
    private val border by lazy { getColor(R.color.costudy_border) }
    private val danger by lazy { getColor(R.color.costudy_danger) }
    private val dangerSurface by lazy { getColor(R.color.costudy_danger_surface) }
    private val white by lazy { getColor(R.color.costudy_surface) }
    private val screens by lazy { AdminScreenCatalog.all() }
    private val viewModel by lazy { ViewModelProvider(this)[AdminPrototypeViewModel::class.java] }
    private lateinit var binding: ActivityAdminPrototypeBinding
    private lateinit var root: LinearLayout

    private val session by lazy { DemoSession(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (session.role != DemoRole.ADMIN) {
            finish()
            return
        }
        binding = ActivityAdminPrototypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!viewModel.handleSystemBack()) finish()
            }
        })
        viewModel.screenId.observe(this) { screenId -> show(screenId) }
    }

    override fun onResume() {
        super.onResume()
        if (session.role != DemoRole.ADMIN) finish()
    }

    private fun show(id: Int) {
        val target = screens[id] ?: screens.getValue(1)
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            layoutParams = ViewGroup.LayoutParams(-1, -1)
        }
        binding.adminRoot.removeAllViews()
        binding.adminRoot.addView(root, FrameLayout.LayoutParams(-1, -1))
        val statusBarSpace = View(this)
        root.addView(statusBarSpace, LinearLayout.LayoutParams(-1, 0))
        applyAuthInsets(binding.root, statusBarSpace)
        addHeader(target, id)

        val scroll = ScrollView(this).apply {
            clipToPadding = false
            isFillViewport = false
            setPadding(dp(24), dp(20), dp(24), dp(12))
            layoutParams = LinearLayout.LayoutParams(-1, 0, 1f)
        }
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content)
        if (id == 1) addStats(content)
        target.blocks.forEach { addBlock(content, it, id) }
        target.formHint?.let { addInput(content, it) }
        root.addView(scroll)

        if (target.actions.isNotEmpty()) addActions(target.actions)
        if (hasBottomNavigation(id)) addBottomNavigation(id)
    }
    private fun addHeader(screen: AdminScreen, id: Int) {
        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(white)
            setPadding(dp(24), dp(16), dp(24), dp(16))
            minimumHeight = dp(88)
        }
        if (id != 0 && id != 1 && id != 2 && id != 12 && id != 16 && id != 19) {
            val back = text("‹", 32, ink, true).apply {
                gravity = Gravity.CENTER
                setOnClickListener { viewModel.navigateBack(screen.back) }
            }
            header.addView(back, LinearLayout.LayoutParams(dp(32), dp(48)).apply { rightMargin = dp(12) })
        }
        val labels = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        labels.addView(text(getString(R.string.admin_prototype_label), 12, green, true))
        labels.addView(text(screen.title, 24, ink, true))
        if (screen.subtitle.isNotBlank()) {
            labels.addView(text(screen.subtitle, 12, muted).apply { setPadding(0, dp(3), 0, 0) })
        }
        header.addView(labels, LinearLayout.LayoutParams(0, -2, 1f))
        root.addView(header)
    }

    private fun addStats(parent: LinearLayout) {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        listOf("128" to "Thành viên", "2" to "Chuyển cấp", "12" to "Kỹ năng").forEachIndexed { i, pair ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                background = rounded(white, 16)
                addView(text(pair.first, 18, green, true).apply { gravity = Gravity.CENTER })
                addView(text(pair.second, 10, muted).apply { gravity = Gravity.CENTER; setPadding(0, dp(4), 0, 0) })
            }
            row.addView(card, LinearLayout.LayoutParams(0, dp(66), 1f).apply { if (i > 0) leftMargin = dp(12) })
        }
        parent.addView(row, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
    }

    private fun addBlock(parent: LinearLayout, block: AdminBlock, screen: Int) {
        if (block.kind == 2) {
            val field = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            field.addView(text(block.heading.uppercase(), 10, muted, true).apply { setPadding(0, 0, 0, dp(6)) })
            val input = EditText(this).apply {
                textSize = 14f
                setTextColor(ink)
                setHintTextColor(muted)
                setText(block.body)
                setPadding(dp(16), dp(12), dp(16), dp(12))
                background = rounded(white, 14, border)
                minHeight = dp(56)
                if (block.heading.contains("mật khẩu", ignoreCase = true)) {
                    inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
            field.addView(input)
            parent.addView(field, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
            return
        }
        if (block.kind == 3 && block.heading == "Thống kê") {
            val metrics = block.body.split(Regex("\\s{2,}")).mapNotNull { segment ->
                val parts = segment.trim().split(Regex("\\s+"), limit = 2)
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            metrics.forEachIndexed { index, metric ->
                val metricCard = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER
                    background = rounded(white, 16)
                    addView(text(metric.first, 18, green, true).apply { gravity = Gravity.CENTER })
                    addView(text(metric.second, 10, muted).apply { gravity = Gravity.CENTER; setPadding(0, dp(4), 0, 0) })
                }
                row.addView(metricCard, LinearLayout.LayoutParams(0, dp(66), 1f).apply { if (index > 0) leftMargin = dp(12) })
            }
            parent.addView(row, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
            return
        }
        val card = LinearLayout(this).apply {
            orientation = if (block.kind == 4) LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
            gravity = if (block.kind == 4) Gravity.CENTER_VERTICAL else Gravity.TOP
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(when (block.kind) { 1 -> if (screen == 1) ink else mint; else -> white }, 16)
        }
        val fg = if (block.kind == 1 && screen == 1) white else ink
        if (block.kind == 4) {
            val copy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            copy.addView(text(block.heading, 14, ink, true))
            if (block.body.isNotBlank()) copy.addView(text(block.body, 12, muted).apply { setPadding(0, dp(4), 0, 0); setLineSpacing(dp(2).toFloat(), 1f) })
            card.addView(copy, LinearLayout.LayoutParams(0, -2, 1f))
            card.addView(text("→", 18, muted, true).apply { gravity = Gravity.CENTER; setPadding(dp(12), 0, 0, 0) })
        } else if (block.kind == 3) {
            card.addView(text(block.heading, 18, green, true))
            card.addView(text(block.body, 10, muted).apply { setPadding(0, dp(4), 0, 0) })
        } else {
            val sectionLabel = block.heading.isNotBlank() && block.heading == block.heading.uppercase() && block.heading.any { it.isLetter() }
            if (block.heading.isNotBlank()) card.addView(text(block.heading, if (sectionLabel) 10 else 18, if (sectionLabel) muted else fg, block.kind == 1 || sectionLabel))
            if (block.body.isNotBlank()) card.addView(text(block.body, 14, if (block.kind == 1 && screen == 1) white else muted).apply {
                setPadding(0, dp(if (block.heading.isBlank()) 0 else 6), 0, 0)
            })
        }
        val target = when (screen) {
            1 -> when (block.heading) { "Báo cáo chuyển cấp" -> 12; "Quản lý thành viên" -> 2; "Nhật ký gần đây" -> 22; "Hồ sơ quản trị viên" -> 23; else -> null }
            2 -> when (block.heading) { "TÀI KHOẢN CẦN XEM XÉT" -> 3; "PHÂN QUYỀN NHÂN SỰ" -> 9; else -> null }
            3 -> if (block.heading == "CS-202610-002") 13 else null
            12 -> if (block.heading.startsWith("CS-202610-002")) 13 else if (block.heading.startsWith("CS-202610-003")) 35 else null
            13 -> if (block.heading == "Xem lịch sử tài khoản") 3 else null
            16 -> if (block.heading == "LẬP TRÌNH") 17 else null
            19 -> when (block.heading) { "Cấu hình cộng đồng" -> 20; "Chuỗi học tập và huy hiệu" -> 25; "Hồ sơ lượt học chuyển cấp" -> 28; "Nhật ký quản trị" -> 22; "Hồ sơ Admin" -> 23; else -> null }
            23 -> if (block.heading == "Nhật ký quản trị") 22 else null
            else -> null
        }
        if (target != null) card.setOnClickListener { viewModel.navigate(target) }
        parent.addView(card, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
    }

    private fun addInput(parent: LinearLayout, hint: String) {
        val input = EditText(this).apply {
            textSize = 14f
            setTextColor(ink)
            setHintTextColor(muted)
            setHint(hint)
            setPadding(dp(16), dp(14), dp(16), dp(14))
            background = rounded(white, 14, border)
            minHeight = dp(56)
        }
        parent.addView(input, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(16) })
    }

    private fun addActions(actions: List<AdminAction>) {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(12), dp(24), dp(16))
            setBackgroundColor(bg)
        }
        actions.forEachIndexed { index, action ->
            val button = Button(this).apply {
                text = action.label
                textSize = 14f
                isAllCaps = false
                setTextColor(if (action.secondary) ink else if (action.danger) danger else white)
                background = rounded(if (action.secondary) white else if (action.danger) dangerSurface else ink, 16, if (action.secondary) border else if (action.danger) danger else null)
                setOnClickListener { viewModel.navigate(action.destination) }
            }
            container.addView(button, LinearLayout.LayoutParams(-1, dp(52)).apply { if (index > 0) topMargin = dp(8) })
        }
        root.addView(container)
    }

    private fun hasBottomNavigation(id: Int) = id == 1 || id == 2 || id == 12 || id == 16 || id == 19

    private fun addBottomNavigation(id: Int) {
        val tabs = listOf(1 to "⌂\nTổng quan", 2 to "♙\nTài khoản", 12 to "▤\nBáo cáo", 16 to "▦\nDanh mục", 19 to "⚙\nHệ thống")
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            background = rounded(white, 16)
        }
        tabs.forEach { (destination, label) ->
            val active = id == destination
            val item = TextView(this).apply {
                text = label
                gravity = Gravity.CENTER
                textSize = 10f
                setLineSpacing(0f, 0.9f)
                setTextColor(if (active) green else muted)
                background = if (active) rounded(mint, 16) else null
                setOnClickListener { if (!active) viewModel.navigate(destination) }
            }
            bar.addView(item, LinearLayout.LayoutParams(0, dp(56), 1f))
        }
        val wrap = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(12), dp(24), dp(16))
            setBackgroundColor(bg)
            addView(bar, LinearLayout.LayoutParams(-1, dp(56)))
        }
        root.addView(wrap, LinearLayout.LayoutParams(-1, -2))
    }

    private fun text(value: String, size: Int, color: Int, bold: Boolean = false) = TextView(this).apply {
        text = value
        textSize = size.toFloat()
        setTextColor(color)
        typeface = if (bold) Typeface.create("sans-serif", Typeface.BOLD) else Typeface.create("sans-serif", Typeface.NORMAL)
        setLineSpacing(dp(2).toFloat(), 1f)
    }

    private fun rounded(color: Int, radiusDp: Int, stroke: Int? = null): GradientDrawable = GradientDrawable().apply {
        setColor(color)
        cornerRadius = dp(radiusDp).toFloat()
        stroke?.let { setStroke(dp(1), it) }
    }

    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()
}
