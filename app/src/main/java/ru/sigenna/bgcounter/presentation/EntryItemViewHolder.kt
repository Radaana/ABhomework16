package ru.sigenna.bgcounter.presentation

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.sigenna.bgcounter.R
import ru.sigenna.bgcounter.model.BgEntryData
import ru.sigenna.bgcounter.model.BgWeight
import ru.sigenna.bgcounter.utils.Utils.formatDaleFromLong

class EntryItemViewHolder (
    private val view: View,
) : RecyclerView.ViewHolder(view) {

    private val title: TextView by lazy { view.findViewById(R.id.itemTitle) }
    private val date: TextView by lazy { view.findViewById(R.id.itemDate) }
    private val qty: TextView by lazy { view.findViewById(R.id.itemQty) }
    private val weight: TextView by lazy { view.findViewById(R.id.itemWeight) }
    private val starIcon: ImageView by lazy { view.findViewById(R.id.itemStar) }
    private val shelfIcon: ImageView by lazy { view.findViewById(R.id.itemShelf) }
    private val unboxIcon: ImageView by lazy { view.findViewById(R.id.itemUnbox) }

    fun bind(item: BgEntryData) {
        title.text = item.title
        date.text = formatDaleFromLong(item.date)
        qty.text = item.qty.toString()
        if (item.isMine && item.isNew) {
            unboxIcon.visibility = View.VISIBLE
        } else {
            unboxIcon.visibility = View.GONE
        }
        if (item.isMine && !item.isNew) {
            shelfIcon.visibility = View.VISIBLE
        } else {
            shelfIcon.visibility = View.GONE
        }
        if (item.isNew && !item.isMine) {
            starIcon.visibility = View.VISIBLE
        } else {
            starIcon.visibility = View.GONE
        }
        weight.text = when (item.weight) {
            BgWeight.EASY ->  view.resources.getString(R.string.easy);
            BgWeight.MIDDLE -> view.resources.getString(R.string.middle);
            BgWeight.HEAVY -> view.resources.getString(R.string.heavy);
            null -> ""
        }
    }
}