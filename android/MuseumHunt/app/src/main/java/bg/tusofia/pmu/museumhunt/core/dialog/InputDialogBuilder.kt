package bg.tusofia.pmu.museumhunt.core.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import bg.tusofia.pmu.museumhunt.R

class InputDialogBuilder(context: Context, @StyleRes theme: Int) : AlertDialog.Builder(context, theme) {

    private var view: View? = null

    fun setPositiveButton(@StringRes stringId: Int, callback: (String, DialogInterface, Int) -> Unit): InputDialogBuilder {

        setPositiveButton(stringId) { dialog, which ->
            view?.let {
                val text = it.findViewById<EditText>(R.id.et_name).text.toString()
                callback(text, dialog, which)
            }
        }

        return this
    }

    override fun setView(view: View?): InputDialogBuilder {
        this.view = view
        super.setView(view)
        return this
    }
}