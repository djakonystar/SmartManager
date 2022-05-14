package uz.texnopos.smartmanager.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.MaterialTimePicker.INPUT_MODE_CLOCK
import com.google.android.material.timepicker.TimeFormat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.extensions.showError
import uz.texnopos.smartmanager.core.extensions.showSuccess
import uz.texnopos.smartmanager.core.utils.ResourceState
import uz.texnopos.smartmanager.data.models.bot.EditChatId
import uz.texnopos.smartmanager.data.models.bot.EditTime
import uz.texnopos.smartmanager.databinding.FragmentSettingsBinding
import uz.texnopos.smartmanager.settings.Settings

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    private val settings: Settings by inject()
    private var startHour = ""
    private var startMinute = ""
    private var endHour = ""
    private var endMinute = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSettingsBinding.bind(view)

        binding.apply {
            if (settings.role == "ADMIN") {
                tilChatId.isVisible = false
                ivApplyChatId.isVisible = false
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                setLoading(false)
                viewModel.getRules()
            }

            etTimeRange.setOnFocusChangeListener { _, b ->
                if (b) {
                    tilTimeRange.isEnabled = false
                    val startTimePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(startHour.ifEmpty { "0" }.toInt())
                        .setMinute(startMinute.ifEmpty { "0" }.toInt())
                        .setTitleText(getString(R.string.start_time))
                        .setInputMode(INPUT_MODE_CLOCK)
                        .build()

                    startTimePicker.show(
                        requireActivity().supportFragmentManager,
                        startTimePicker.tag
                    )
                    startTimePicker.addOnPositiveButtonClickListener {
                        val startHourT = startTimePicker.hour.toString()
                        val startMinuteT = startTimePicker.minute.toString()

                        val timePicker = MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(endHour.ifEmpty { "0" }.toInt())
                            .setMinute(endMinute.ifEmpty { "0" }.toInt())
                            .setTitleText(getString(R.string.end_time))
                            .setInputMode(INPUT_MODE_CLOCK)
                            .build()

                        timePicker.show(requireActivity().supportFragmentManager, timePicker.tag)
                        timePicker.addOnPositiveButtonClickListener {
                            startHour = startHourT
                            startMinute = startMinuteT
                            endHour = timePicker.hour.toString()
                            endMinute = timePicker.minute.toString()

                            etTimeRange.setText(
                                getString(
                                    R.string.time_range_template,
                                    startHour,
                                    startMinute,
                                    endHour,
                                    endMinute
                                )
                            )
                        }
                    }

                    startTimePicker.addOnDismissListener {
                        tilTimeRange.isEnabled = true
                    }
                }
            }

            ivApplyDate.onClick {
                viewModel.editTime(EditTime(startHour, endHour, startMinute, endMinute))
            }

            etChatId.addTextChangedListener {
                tilChatId.isErrorEnabled = false
            }

            ivApplyChatId.onClick {
                val chatId = etChatId.text.toString()

                if (chatId.isNotEmpty()) {
                    viewModel.editChatId(EditChatId(chatId))
                } else {
                    tilChatId.error = getString(R.string.required_field)
                }
            }
        }

        viewModel.getRules()
        setUpObservers()
    }

    private fun setLoading(loading: Boolean) {
        binding.apply {
            progressCircular.isVisible = loading
            tilTimeRange.isEnabled = !loading
            tilChatId.isEnabled = !loading
            ivApplyDate.isEnabled = !loading
            ivApplyChatId.isEnabled = !loading
        }
    }

    private fun setUpObservers() {
        viewModel.rules.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data?.let { rule ->
                        binding.apply {
                            startHour = rule.startHour.formatTime()
                            startMinute = rule.startMinute.formatTime()
                            endHour = rule.endHour.formatTime()
                            endMinute = rule.endMinute.formatTime()

                            etTimeRange.setText(
                                getString(
                                    R.string.time_range_template,
                                    startHour,
                                    startMinute,
                                    endHour,
                                    endMinute
                                )
                            )
                            etChatId.setText(rule.chatId)
                            tvSendTimeMessage.text = getString(
                                R.string.send_time_message,
                                rule.sendHour.formatTime(),
                                rule.sendMinute.formatTime()
                            )
                        }
                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }

        viewModel.editTime.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    showSuccess(getString(R.string.time_range_setted_successfully))
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }

        viewModel.editChatId.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    showSuccess(getString(R.string.chat_id_setted_successfully))
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }
    }

    private fun String.formatTime(): String {
        return if (this.length == 1) {
            val chars = this.toMutableList()
            chars.add(0, '0')
            chars.joinToString("")
        } else this
    }
}
