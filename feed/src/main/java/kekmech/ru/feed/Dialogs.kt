package kekmech.ru.feed

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog

object Dialogs {
    fun listDialog(context: Context, title: String, list: List<String>, selectListener: (DialogInterface, Int) -> Unit) =
        AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(list.toTypedArray(), selectListener)
            .create()
}