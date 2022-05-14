package uz.texnopos.smartmanager.ui.admin

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.inflate
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.core.utils.BaseAdapter
import uz.texnopos.smartmanager.data.models.user.Admin
import uz.texnopos.smartmanager.databinding.ItemAdminBinding

class AdminAdapter : BaseAdapter<Admin, AdminAdapter.AdminViewHolder>() {
    inner class AdminViewHolder(private val binding: ItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(admin: Admin) {
            binding.apply {
                tvFullname.text = itemView.context.getString(
                    R.string.full_name,
                    admin.firstName ?: "",
                    admin.lastName ?: ""
                )
                tvUsername.text = admin.username ?: ""

                tvFullname.isSelected = true
                tvUsername.isSelected = true

                ivEdit.onClick {
                    onEditClick(admin)
                }

                ivDelete.onClick {
                    onDeleteClick(admin)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val itemView = parent.inflate(R.layout.item_admin)
        val binding = ItemAdminBinding.bind(itemView)
        return AdminViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.bind(models[position])
    }

    private var onEditClick: (admin: Admin) -> Unit = {}
    fun setOnEditClickListener(onEditClick: (admin: Admin) -> Unit) {
        this.onEditClick = onEditClick
    }

    private var onDeleteClick: (admin: Admin) -> Unit = {}
    fun setOnDeleteClickListener(onDeleteClick: (admin: Admin) -> Unit) {
        this.onDeleteClick = onDeleteClick
    }
}
