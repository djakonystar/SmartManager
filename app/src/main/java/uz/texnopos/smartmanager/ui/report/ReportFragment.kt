package uz.texnopos.smartmanager.ui.report

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.changeDateFormat
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.extensions.showError
import uz.texnopos.smartmanager.core.utils.CalendarHelper
import uz.texnopos.smartmanager.core.utils.ResourceState
import uz.texnopos.smartmanager.data.models.report.ReportDate
import uz.texnopos.smartmanager.databinding.FragmentReportBinding
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : Fragment(R.layout.fragment_report) {
    private lateinit var binding: FragmentReportBinding
    private val viewModel: ReportViewModel by viewModel()
    private val adapter: ReportAdapter by inject()
    private val calendarHelper = CalendarHelper()
    private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.ROOT)
    private var dateFrom = calendarHelper.currentDate
    private var dateFromInLong = calendarHelper.currentDateMillis
    private var dateTo = calendarHelper.currentDate
    private var dateToInLong = calendarHelper.currentDateMillis
    private lateinit var reportDate: ReportDate
    private val supervisorsList = mutableListOf<String>()
    private val supervisorsIds = mutableListOf<Int>()
    private var selectedSupervisorId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentReportBinding.bind(view)
        applyDate()
        resetSupervisor()

        binding.apply {
            actvSupervisor.threshold = 100
            actvSupervisor.setOnItemClickListener { _, _, i, _ ->
                selectedSupervisorId = i
                if (selectedSupervisorId == 0) viewModel.getReports(reportDate)
                else viewModel.getSupervisorsReports(selectedSupervisorId, reportDate)
            }

            swipeRefresh.setOnRefreshListener {
                swipeRefresh.isRefreshing = false
                setLoading(false)
                resetSupervisor()
                viewModel.getReports(reportDate)
            }

            recyclerView.adapter = adapter
            adapter.setOnItemClickListener {
                val browse = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                startActivity(browse)
            }

            fabDatePicker.onClick {
                val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
                    .setSelection(
                        androidx.core.util.Pair(dateFromInLong, dateToInLong)
                    )
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setValidator(DateValidatorPointBackward.before(calendarHelper.currentDateMillis))
                            .build()
                    )
                    .setTitleText(R.string.choose_range)
                    .build()

                dateRangePicker.addOnPositiveButtonClickListener { dates ->
                    dateFromInLong = dates.first
                    dateFrom = simpleDateFormat.format(dateFromInLong)
                    dateToInLong = dates.second
                    dateTo = simpleDateFormat.format(dateToInLong)

                    applyDate()
                    if (selectedSupervisorId == 0) viewModel.getReports(reportDate)
                    else viewModel.getSupervisorsReports(selectedSupervisorId, reportDate)
                }

                dateRangePicker.addOnDismissListener {
                    fabDatePicker.isEnabled = true
                }

                dateRangePicker.show(requireActivity().supportFragmentManager, dateRangePicker.tag)
                fabDatePicker.isEnabled = false
            }
        }

        viewModel.getReports(reportDate)
        setUpObservers()
    }

    override fun onDetach() {
        adapter.models = listOf()
        super.onDetach()
    }

    private fun setLoading(loading: Boolean) {
        binding.apply {
            progressCircular.isVisible = loading
            tilSupervisor.isEnabled = !loading
            swipeRefresh.isEnabled = !loading
            recyclerView.isEnabled = !loading
            fabDatePicker.isEnabled = !loading
        }
    }

    private fun setUpObservers() {
        viewModel.reports.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data?.let { reports ->
                        adapter.models = reports
                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }

        viewModel.supervisors.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data!!.forEach { supervisor ->
                        supervisorsList.add(
                            getString(
                                R.string.full_name,
                                supervisor.firstName ?: "",
                                supervisor.lastName ?: ""
                            )
                        )
                        supervisorsIds.add(supervisor.id)
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.item_spinner,
                            supervisorsList
                        )
                        binding.actvSupervisor.setAdapter(adapter)
                        binding.actvSupervisor.setText(supervisorsList[0])
                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }

        viewModel.supervisorsReports.observe(viewLifecycleOwner) {
            when (it.status) {
                ResourceState.LOADING -> setLoading(true)
                ResourceState.SUCCESS -> {
                    setLoading(false)
                    it.data?.let { reports ->
                        adapter.models = reports
                    }
                }
                ResourceState.ERROR -> {
                    setLoading(false)
                    showError(it.message)
                }
            }
        }
    }

    private fun applyDate() {
        binding.toolbar.subtitle = getString(R.string.date_duration, dateFrom, dateTo)
        reportDate = ReportDate(dateFrom.changeDateFormat, dateTo.changeDateFormat)
    }

    private fun resetSupervisor() {
        supervisorsList.clear()
        supervisorsIds.clear()
        supervisorsList.add(getString(R.string.all_supervisors))
        supervisorsIds.add(0)
        selectedSupervisorId = 0
        binding.actvSupervisor.setText(supervisorsList[0])
        viewModel.getSupervisors()
    }
}
