package uz.texnopos.smartmanager.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import uz.texnopos.smartmanager.R
import uz.texnopos.smartmanager.core.extensions.onClick
import uz.texnopos.smartmanager.databinding.DialogWarningBinding

class WarningDialog(private val message: String) : DialogFragment(R.layout.dialog_warning) {
    private lateinit var binding: DialogWarningBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.shape_dialog)
        dialog!!.setCanceledOnTouchOutside(false)
        return inflater.inflate(R.layout.dialog_warning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogWarningBinding.bind(view)

        binding.apply {
            tvMessage.text = message

            btnYes.onClick {
                onPositiveButtonClick.invoke()
                dismiss()
            }

            btnCancel.onClick {
                dismiss()
            }
        }
    }

    private var onPositiveButtonClick: () -> Unit = {}
    fun setOnPositiveButtonClickListener(onPositiveButtonClick: () -> Unit) {
        this.onPositiveButtonClick = onPositiveButtonClick
    }
}
