package com.ute.co_study.shared.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.text.InputFilter
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.button.MaterialButton
import com.ute.co_study.R
import com.ute.co_study.feature.auth.ui.applyAuthInsets
import com.ute.co_study.shared.data.DemoSession
import com.ute.co_study.shared.data.DemoRole

abstract class WorkflowActivity(
    @LayoutRes private val layout: Int,
    private val requiredRole: DemoRole? = null
) : AppCompatActivity() {
    internal val session by lazy { DemoSession(this) }
    protected lateinit var body: LinearLayout
        private set
    protected lateinit var actions: LinearLayout
        private set
    private val footer get() = actions
    private val fields = linkedMapOf<String, EditText>()
    private val choices = linkedMapOf<String, TextView>()
    private var drafts = Bundle()
    private var displayed = false
    protected open val refreshOnReturn = false

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!canOpen()) {
            finish()
            return
        }
        drafts = savedInstanceState?.getBundle("workflow_fields") ?: Bundle()
        setContentView(layout)
        val root = findViewById<View>(R.id.workflow_root)
        applyAuthInsets(root, findViewById(R.id.workflow_status))
        ViewCompat.setAccessibilityHeading(findViewById(R.id.workflow_title), true)
        findViewById<View>(R.id.workflow_back)?.setOnClickListener { finish() }
        body = findViewById(R.id.workflow_body)
        actions = findViewById(R.id.workflow_footer)
    }

    final override fun onResume() {
        super.onResume()
        if (!canOpen()) {
            finish()
            return
        }
        if (!displayed || refreshOnReturn) refresh(preserveInputs = displayed)
        displayed = true
    }

    final override fun onSaveInstanceState(outState: Bundle) {
        outState.putBundle("workflow_fields", currentInputs())
        super.onSaveInstanceState(outState)
    }

    protected abstract fun render()

    private fun currentInputs() = Bundle().apply {
        fields.forEach { (key, field) -> putString(key, field.text.toString()) }
        choices.forEach { (key, choice) -> putString(key, choice.text.toString()) }
    }

    protected fun refresh(preserveInputs: Boolean = false) {
        if (preserveInputs) drafts = currentInputs()
        fields.clear()
        choices.clear()
        body.removeAllViews()
        footer.removeAllViews()
        render()
        val configuration = resources.configuration
        if (configuration.screenHeightDp < 500 || configuration.fontScale > 1.4f) {
            (footer.parent as? ViewGroup)?.removeView(footer)
            footer.setPadding(0, gap, 0, 0)
            if (footer.childCount > 0) add(body, footer)
        }
        drafts.clear()
    }

    private fun canOpen() = requiredRole == null || session.role == requiredRole

    protected fun open(target: Class<out AppCompatActivity>, data: Bundle = Bundle()) {
        startActivity(Intent(this, target).putExtras(data).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }

    protected fun returnTo(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
    }

    protected fun replaceTask(target: Class<out AppCompatActivity>) {
        startActivity(Intent(this, target).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }

    protected fun status(subtitle: CharSequence) {
        findViewById<TextView>(R.id.workflow_subtitle).text = subtitle
    }

    protected fun label(text: CharSequence, parent: LinearLayout = body): TextView =
        textView(parent, text, R.style.TextAppearance_CoStudy_Subtitle)

    protected fun card(title: CharSequence, detail: CharSequence = "", tone: Tone = Tone.PLAIN) {
        val panel = panel(tone)
        if (title.isNotEmpty()) textView(panel, title, R.style.TextAppearance_CoStudy_Heading, tone)
        if (detail.isNotEmpty()) textView(panel, detail, R.style.TextAppearance_CoStudy_Body, tone, title.isNotEmpty())
    }

    protected fun info(label: CharSequence, value: CharSequence) {
        val panel = panel()
        textView(panel, label, R.style.TextAppearance_CoStudy_Subtitle)
        textView(panel, value, R.style.TextAppearance_CoStudy_Body, separated = true)
    }

    protected fun note(text: CharSequence, tone: Tone = Tone.MINT) = card("", text, tone)

    protected fun person(initials: String, name: String, detail: String, parent: LinearLayout = body, action: (() -> Unit)? = null): View {
        val panel = panel(parent = parent)
        panel.orientation = LinearLayout.HORIZONTAL
        panel.gravity = Gravity.CENTER_VERTICAL
        val avatar = TextView(this).apply {
            text = initials
            textSize = 17f
            gravity = Gravity.CENTER
            setTextColor(color(R.color.co_study_green))
            setBackgroundResource(R.drawable.bg_co_study_mint_circle)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }
        val avatarSize = dp(R.dimen.workflow_avatar)
        panel.addView(avatar, LinearLayout.LayoutParams(avatarSize, avatarSize))
        val copy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        panel.addView(copy, LinearLayout.LayoutParams(0, wrap, 1f).apply { marginStart = gap })
        textView(copy, name, R.style.TextAppearance_CoStudy_Heading)
        textView(copy, detail, R.style.TextAppearance_CoStudy_Subtitle, separated = true)
        if (action != null) makeRowClickable(panel, action)
        return panel
    }

    protected fun row(title: CharSequence, detail: CharSequence = "", action: () -> Unit) {
        val panel = panel()
        panel.orientation = LinearLayout.HORIZONTAL
        panel.gravity = Gravity.CENTER_VERTICAL
        val copy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        panel.addView(copy, LinearLayout.LayoutParams(0, wrap, 1f))
        textView(copy, title, R.style.TextAppearance_CoStudy_Heading)
        if (detail.isNotEmpty()) textView(copy, detail, R.style.TextAppearance_CoStudy_Subtitle, separated = true)
        makeRowClickable(panel, action)
    }

    private fun makeRowClickable(panel: LinearLayout, action: () -> Unit) {
        val arrow = ImageView(this).apply {
            setImageResource(R.drawable.ic_forward)
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }
        val size = dp(R.dimen.co_study_space_16)
        panel.addView(arrow, LinearLayout.LayoutParams(size, size).apply { marginStart = gap })
        val foregroundAttr = android.util.TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, foregroundAttr, true)
        panel.foreground = ContextCompat.getDrawable(this, foregroundAttr.resourceId)
        panel.minimumHeight = dp(R.dimen.workflow_touch_target)
        panel.isFocusable = true
        ViewCompat.setScreenReaderFocusable(panel, true)
        panel.setOnClickListener { action() }
    }

    protected fun metrics(vararg metrics: Pair<String, String>) {
        val line = LinearLayout(this)
        add(body, line)
        metrics.forEachIndexed { index, (value, name) ->
            val tile = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setBackgroundResource(R.drawable.bg_co_study_card)
                setPadding(gap, gap, gap, gap)
            }
            line.addView(tile, LinearLayout.LayoutParams(0, match, 1f).apply {
                if (index > 0) marginStart = dp(R.dimen.co_study_space_8)
            })
            textView(tile, value, R.style.TextAppearance_CoStudy_Heading, Tone.MINT).gravity = Gravity.CENTER
            textView(tile, name, R.style.TextAppearance_CoStudy_Subtitle, separated = true).gravity = Gravity.CENTER
        }
    }

    protected fun result(title: String, detail: String, negative: Boolean = false) {
        val frame = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(gap, gap, gap, gap)
        }
        add(body, frame)
        val tone = if (negative) Tone.DANGER else Tone.MINT
        val symbol = textView(frame, if (negative) "×" else "✓", R.style.TextAppearance_CoStudy_Title, tone)
        val size = dp(R.dimen.reset_confirmation_icon_size)
        symbol.layoutParams = LinearLayout.LayoutParams(size, size)
        symbol.gravity = Gravity.CENTER
        symbol.setBackgroundResource(if (negative) R.drawable.bg_workflow_danger_circle else R.drawable.bg_co_study_mint_circle)
        symbol.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        textView(frame, title, R.style.TextAppearance_CoStudy_Heading, separated = true).gravity = Gravity.CENTER
        textView(frame, detail, R.style.TextAppearance_CoStudy_Body, separated = true).gravity = Gravity.CENTER
    }

    protected fun input(
        label: String,
        value: String = "",
        hint: String = "",
        multiline: Boolean = false,
        type: Int = InputType.TYPE_CLASS_TEXT,
        maxLength: Int = 500,
        parent: LinearLayout = body
    ): EditText {
        val container = panel(parent = parent)
        val caption = textView(container, label, R.style.TextAppearance_CoStudy_Subtitle)
        val editor = EditText(this).apply {
            id = View.generateViewId()
            inputType = if (multiline) type or InputType.TYPE_TEXT_FLAG_MULTI_LINE else type
            if (multiline) maxLines = 5 else setSingleLine(true)
            filters = arrayOf(InputFilter.LengthFilter(maxLength))
            this.hint = hint
            setText(drafts.getString(label) ?: value)
            textSize = 14f
            setTextColor(color(R.color.co_study_ink))
            setHintTextColor(color(R.color.co_study_muted))
            background = null
            minHeight = dp(R.dimen.workflow_touch_target)
            setPadding(0, 0, 0, 0)
        }
        caption.labelFor = editor.id
        container.addView(editor, LinearLayout.LayoutParams(match, wrap))
        fields[label] = editor
        return editor
    }

    protected fun choice(label: String, options: List<String>, selected: String = options.first()): TextView {
        val current = (drafts.getString(label) ?: selected).takeIf(options::contains) ?: options.first()
        val panel = panel().apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val copy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        panel.addView(copy, LinearLayout.LayoutParams(0, wrap, 1f))
        textView(copy, label, R.style.TextAppearance_CoStudy_Subtitle)
        val value = textView(copy, current, R.style.TextAppearance_CoStudy_Body, separated = true)
        makeRowClickable(panel) {
            AlertDialog.Builder(this)
                .setTitle(label)
                .setSingleChoiceItems(options.toTypedArray(), options.indexOf(value.text.toString())) { dialog, index ->
                    value.text = options[index]
                    dialog.dismiss()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
        choices[label] = value
        return value
    }

    protected fun required(field: EditText): String? {
        val value = field.text.toString().trim()
        if (value.isNotEmpty()) return value
        field.error = getString(R.string.workflow_required)
        field.requestFocus()
        return null
    }

    protected fun button(
        title: CharSequence,
        tone: Tone = Tone.PRIMARY,
        parent: LinearLayout = footer,
        action: () -> Unit
    ): MaterialButton {
        val style = if (tone == Tone.PRIMARY) R.style.Widget_CoStudy_Button_Primary else R.style.Widget_CoStudy_Button_Outlined
        val control = MaterialButton(ContextThemeWrapper(this, style), null, 0)
        control.text = title
        control.minimumHeight = dp(R.dimen.workflow_button_height)
        control.cornerRadius = dp(R.dimen.co_study_radius_card)
        control.isAllCaps = false
        val text = when (tone) {
            Tone.PRIMARY -> R.color.white
            Tone.DANGER -> R.color.co_study_danger
            else -> R.color.co_study_ink
        }
        val background = when (tone) {
            Tone.PRIMARY -> R.color.co_study_ink
            Tone.DANGER -> R.color.co_study_danger_surface
            Tone.MINT -> R.color.co_study_mint
            else -> R.color.white
        }
        control.setTextColor(color(text))
        control.backgroundTintList = ColorStateList.valueOf(color(background))
        if (tone != Tone.PRIMARY) {
            control.strokeWidth = dp(R.dimen.workflow_stroke)
            control.strokeColor = ColorStateList.valueOf(color(if (tone == Tone.DANGER) text else R.color.co_study_outline))
        }
        add(parent, control)
        control.setOnClickListener { action() }
        return control
    }

    protected fun tabs(current: Int, tabs: List<Triple<String, Int, () -> Unit>>) {
        val strip = LinearLayout(this).apply { setBackgroundResource(R.drawable.bg_co_study_card) }
        footer.addView(strip, LinearLayout.LayoutParams(match, wrap))
        tabs.forEachIndexed { index, (title, icon, action) ->
            val tab = (layoutInflater.inflate(R.layout.view_navigation_tab, strip, false) as AppCompatButton).apply {
                text = title
                val active = index == current
                setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0)
                val tint = color(if (active) R.color.co_study_green else R.color.co_study_muted)
                setTextColor(tint)
                TextViewCompat.setCompoundDrawableTintList(this, ColorStateList.valueOf(tint))
                if (active) {
                    setBackgroundResource(R.drawable.bg_auth_notice)
                    isSelected = true
                    isClickable = false
                    ViewCompat.setStateDescription(this, getString(R.string.home_tab_current))
                } else setOnClickListener { action() }
            }
            strip.addView(tab)
        }
    }

    protected fun bubble(copy: String, outgoing: Boolean) {
        val tone = if (outgoing) Tone.MINT else Tone.PLAIN
        val bubble = panel(tone)
        val params = bubble.layoutParams as LinearLayout.LayoutParams
        val inset = dp(R.dimen.co_study_space_24)
        if (outgoing) params.marginStart = inset else params.marginEnd = inset
        bubble.layoutParams = params
        textView(bubble, copy, R.style.TextAppearance_CoStudy_Body, tone)
    }

    protected fun revealLatest() {
        val scroll = body.parent as android.widget.ScrollView
        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    protected fun message(text: CharSequence) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

    private fun panel(tone: Tone = Tone.PLAIN, parent: LinearLayout = body): LinearLayout {
        val panel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            val padding = dp(R.dimen.co_study_space_16)
            setPadding(padding, padding, padding, padding)
            setBackgroundResource(when (tone) {
                Tone.PLAIN -> R.drawable.bg_co_study_card
                Tone.PRIMARY -> R.drawable.bg_co_study_primary_card
                Tone.MINT -> R.drawable.bg_auth_notice
                Tone.DANGER -> R.drawable.bg_workflow_danger
            })
        }
        add(parent, panel)
        return panel
    }

    private fun textView(
        parent: LinearLayout,
        value: CharSequence,
        style: Int,
        tone: Tone = Tone.PLAIN,
        separated: Boolean = false
    ): TextView {
        val text = TextView(this, null, 0, style).apply {
            this.text = value
            when (tone) {
                Tone.PRIMARY -> setTextColor(color(R.color.white))
                Tone.MINT -> setTextColor(color(R.color.co_study_green))
                Tone.DANGER -> setTextColor(color(R.color.co_study_danger))
                Tone.PLAIN -> Unit
            }
        }
        parent.addView(text, LinearLayout.LayoutParams(match, wrap).apply {
            if (separated) topMargin = dp(R.dimen.co_study_space_8)
        })
        return text
    }

    private fun add(parent: LinearLayout, child: View) {
        parent.addView(child, LinearLayout.LayoutParams(match, wrap).apply {
            if (parent.childCount > 0) topMargin = gap
        })
    }

    private fun color(id: Int) = ContextCompat.getColor(this, id)
    private fun dp(id: Int) = resources.getDimensionPixelSize(id)
    private val gap get() = dp(R.dimen.co_study_space_12)

    protected enum class Tone { PLAIN, PRIMARY, MINT, DANGER }

    private companion object {
        const val match = ViewGroup.LayoutParams.MATCH_PARENT
        const val wrap = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
