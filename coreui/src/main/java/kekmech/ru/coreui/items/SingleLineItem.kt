package kekmech.ru.coreui.items

import android.view.View
import android.widget.TextView
import kekmech.ru.coreui.R
import kekmech.ru.coreui.adapter.*

class SingleLineItem(
    val string: String,
    itemId: String = ""
) : BaseClickableItem<SingleLineItem.ViewHolder>(itemId) {

    override fun updateViewHolder(viewHolder: ViewHolder) {
        viewHolder.textView.text = string
    }

    override fun approveFactory(factory: BaseFactory) = factory is Factory

    class ViewHolder(view: View) : BaseViewHolder(view) {
        val textView by bind<TextView>(R.id.textViewPrimary)
        override fun onCreateView(view: View) = Unit
    }

    class Factory : BaseFactory(R.layout.item_single_line_layout) {
        override fun instance(view: View) = ViewHolder(view)
    }

    companion object {
        fun buildAdapter() = BaseAdapter.Builder().registerViewTypeFactory(Factory()).build()
    }
}