package uz.texnopos.smartmanager.ui.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.data.models.user.Admin
import uz.texnopos.smartmanager.data.models.user.AdminPost
import uz.texnopos.smartmanager.databinding.DialogAdminBinding

class AddAdminDialog : DialogFragment() {
    private lateinit var binding: DialogAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.shape_dialog)
        return inflater.inflate(R.layout.dialog_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogAdminBinding.bind(view)

        binding.apply {
            tvTitle.text = getString(R.string.add)

            btnPositive.text = getString(R.string.add)

            etFirstname.addTextChangedListener {
                tilFirstname.isErrorEnabled = false
            }
            etLastname.addTextChangedListener {
                tilLastname.isErrorEnabled = false
            }
            etUsername.addTextChangedListener {
                tilUsername.isErrorEnabled = false
            }
            etPassword.addTextChangedListener {
                tilPassword.isErrorEnabled = false
            }

            btnPositive.onClick {
                val firstname = etFirstname.text.toString()
                val lastname = etLastname.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (firstname.isNotEmpty() && lastname.isNotEmpty() &&
                    username.isNotEmpty() && password.isNotEmpty()
                ) {
                    val admin = AdminPost(
                        firstName = firstname,
                        lastName = lastname,
                        username = username,
                        password = password
                    )
                    onPositiveButtonClick(admin)
                } else {
                    if (firstname.isEmpty()) {
                        tilFirstname.error = getString(R.string.required_field)
                    }
                    if (lastname.isEmpty()) {
                        tilLastname.error = getString(R.string.required_field)
                    }
                    if (username.isEmpty()) {
                        tilUsername.error = getString(R.string.required_field)
                    }
                    if (password.isEmpty()) {
                        tilPassword.error = getString(R.string.required_field)
                    }
                }
            }

            btnCancel.onClick { dismiss() }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismiss()
        super.onDismiss(dialog)
    }

    private var onPositiveButtonClick: (admin: AdminPost) -> Unit = {}
    fun setOnPositiveButtonClickListener(onPositiveButtonClick: (admin: AdminPost) -> Unit) {
        this.onPositiveButtonClick = onPositiveButtonClick
    }

    private var onDismiss: () -> Unit = {}
    fun setOnDismissListener(onDismiss: () -> Unit) {
        this.onDismiss = onDismiss
    }
}
