package uz.texnopos.smartmanager.ui.report

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.inflate
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.extensions.parseDate
import uz.texnopos.smartmanager.core.utils.BaseAdapter
import uz.texnopos.smartmanager.data.models.report.Report
import uz.texnopos.smartmanager.databinding.ItemReportBinding

class ReportAdapter : BaseAdapter<Report, ReportAdapter.ReportViewHolder>() {
    inner class ReportViewHolder(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(report: Report) {
            binding.apply {
                tvFullname.text = itemView.context.getString(
                    R.string.full_name,
                    report.supervisor?.firstName ?: "",
                    report.supervisor?.lastName ?: ""
                )
                tvUsername.text = report.supervisor?.username ?: ""
                tvTime.text = report.date
                tvFullname.isSelected = true
                tvUsername.isSelected = true

                root.onClick {
                    onItemClick(report)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val itemView = parent.inflate(R.layout.item_report)
        val binding = ItemReportBinding.bind(itemView)
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(models[position])
    }

    private var onItemClick: (report: Report) -> Unit = {}
    fun setOnItemClickListener(onItemClick: (report: Report) -> Unit) {
        this.onItemClick = onItemClick
    }
}
