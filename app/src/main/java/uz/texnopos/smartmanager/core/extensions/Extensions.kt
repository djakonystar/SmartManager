package uz.texnopos.smartmanager.core.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import uz.texnopos.smartmanager.ui.dialog.SuccessDialog
import uz.texnopos.smartmanager.ui.dialog.WarningDialog
import uz.texnopos.smartmanager.ui.dialog.ErrorDialog
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showMessage(msg: String?) {
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_LONG).show()
}

fun Fragment.showSnackbar(message: String) {
    Snackbar.make(this.requireView(), message, Snackbar.LENGTH_LONG).show()
}

fun Fragment.showError(message: String?): ErrorDialog {
    val errorDialog = ErrorDialog(message!!)
    errorDialog.show(this.requireActivity().supportFragmentManager, errorDialog.tag)
    return errorDialog
}

fun Fragment.showSuccess(message: String?): SuccessDialog {
    val successDialog = SuccessDialog(message!!)
    successDialog.show(this.requireActivity().supportFragmentManager, successDialog.tag)
    return successDialog
}

fun Fragment.showWarning(message: String?): WarningDialog {
    val warningDialog = WarningDialog(message!!)
    warningDialog.show(this.requireActivity().supportFragmentManager, warningDialog.tag)
    return warningDialog
}

fun ViewGroup.inflate(@LayoutRes id: Int): View =
    LayoutInflater.from(context).inflate(id, this, false)

fun RecyclerView.addVertDivider(context: Context?) {
    this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
}

fun RecyclerView.addHorizDivider(context: Context?) {
    this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
}

fun View.onClick(onClick: (View) -> Unit) {
    this.setOnClickListener(onClick)
}

// Setting Html string to TextView and making links clickable
fun TextView.setTextViewHtml(html: String, onLinkClick: (link: String) -> Unit) {
    val sequence: CharSequence = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val strBuilder = SpannableStringBuilder(sequence)
    val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
    for (span in urls) {
        makeLinkClickable(strBuilder, span, onLinkClick)
    }
    this.text = strBuilder
    this.movementMethod = LinkMovementMethod.getInstance()
}

private fun makeLinkClickable(
    strBuilder: SpannableStringBuilder,
    span: URLSpan,
    onLinkClick: (link: String) -> Unit
) {
    val start = strBuilder.getSpanStart(span)
    val end = strBuilder.getSpanEnd(span)
    val flags = strBuilder.getSpanFlags(span)
    val clickable = object : ClickableSpan() {
        override fun onClick(widget: View) {
            val fullText = (widget as TextView).text.toString()
            val link = fullText.substring(start, end)
            onLinkClick.invoke(link)
        }
    }
    strBuilder.setSpan(clickable, start, end, flags)
    strBuilder.removeSpan(span)
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Int.dpToFloat: Float
    get() = (this / Resources.getSystem().displayMetrics.density)

fun String.dialPhone(activity: Activity) {
    var phone = "+998"
    if (this.length == 9) phone += this
    else phone = this
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phone)))
    activity.startActivity(intent)
}

val String.toSumFormat: String
    get() {
        var text = this.reversed()
        text = text.subSequence(0, text.length)
            .chunked(3) // group every 3 chars
            .joinToString(" ")
        return text.reversed()
    }

val String.sumFormat: String
    get() {
        val beforePoint = this.substringBefore('.')
        var text = beforePoint.reversed()
        text = text.chunked(3).joinToString(" ")
        if (this.contains('.')) {
            val afterPoint = this.substringAfter('.')
            return "${text.reversed()}.$afterPoint"
        }
        return text.reversed()
    }

val Int.toSumFormat: String
    get() {
        var text = this.toString().reversed()
        text = text.subSequence(0, text.length)
            .chunked(3) // group every 3 chars
            .joinToString(" ")
        return text.reversed()
    }

val Number.toSumFormat: String
    get() {
        var text = this.toString().reversed()
        text = text.subSequence(0, text.length)
            .chunked(3) // group every 3 chars
            .joinToString(" ")
        return text.reversed()
    }

val Double.toSumFormat: String
    get() {
        var num = this.toLong().toSumFormat
        val formattedNum = "%.${2}f".format(this)
        val afterPoint = formattedNum.substring(formattedNum.length - 2, formattedNum.length)
        num += if (afterPoint == "0") ".00" else {
            if (afterPoint.length == 1) ".${afterPoint}0"
            else ".$afterPoint"
        }
        return num
    }

val String.toPhoneFormat: String
    get() {
        if (this.length == 13) {
            return this.substring(4).toPhoneFormat
        }
        return this.toPhoneNumber
    }

val String.toPhoneNumber: String
    get() {
        val arr = this.toCharArray()
        var phone = "+998 ("
        arr.forEachIndexed { index, c ->
            phone += c
            if (index == 1) {
                phone += ") "
            }
            if (index == 4 || index == 6) {
                phone += " "
            }
        }
        return phone
    }

val String.changeDateFormat: String
    get() {
        val day: String
        val month: String
        val year: String
        return if (this.contains('-')) {
            day = this.substring(8..9)
            month = this.substring(5..6)
            year = this.substring(0..3)
            "$year-$month-$day"
        } else {
            day = this.substring(0..1)
            month = this.substring(3..4)
            year = this.substring(6..9)
            "$year-$month-$day"
        }
    }

val String.parseDate: String
    get() {
        val date = this
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ROOT)
        val result = sdf.parse(date)
        val newSdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT)
        newSdf.timeZone = TimeZone.getTimeZone("GMT+5:00")
        return newSdf.format(result ?: "")
    }

val EditText.filterForDouble: Unit
    @SuppressLint("SetTextI18n")
    get() {
        val filter = InputFilter { source, _, _, spanned, _, _ ->
            val afterPoint = if (spanned.contains('.')) {
                spanned.toString().substringAfter('.').length
            } else {
                0
            }

            if (source != null && source.equals(".") && spanned.contains(".")) ""
            else if (source != null && afterPoint == 2) ""
            else if (source != null && source.equals(".") && spanned.isEmpty()) "0."
            else if (source != null && source.equals(".") && spanned.isNotEmpty()) "."
            else if (source != null && "-,.".contains("" + source)) ""
            else null
        }
        this.filters = arrayOf(filter)
    }

fun EditText.setBlockFilter(block: String) {
    val filter = InputFilter { source, _, _, _, _, _ ->
        if (source != null && block.contains("" + source)) "" else null
    }
    this.filters = arrayOf(filter)
}

val Double.checkModule: Number
    get() {
        return if (this % 1 == 0.0) this.toLong()
        else this
    }

infix fun Double.format(afterPoint: Int): String {
    val formatted = "%.${afterPoint}f".format(this)

    return if (formatted.contains(',')) {
        formatted.replace(',', '.')
    } else {
        formatted.replace('.', ',')
    }
}

val String.toDouble: Double
    get() = this.ifEmpty { "0.0" }.toDouble()
