package ru.sigenna.bgcounter.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.sigenna.bgcounter.R
import ru.sigenna.bgcounter.model.BgEntryData

class EntryListAdapter(private val listener: Listener) :
    ListAdapter<BgEntryData, RecyclerView.ViewHolder>(DiffUtilItem()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.entry_item, parent, false)
        return EntryItemViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList.getOrNull(position)
        if (item != null) {
            (holder as EntryItemViewHolder).bind(item)
        }
    }
}

class DiffUtilItem : DiffUtil.ItemCallback<BgEntryData>() {

    override fun areItemsTheSame(oldItem: BgEntryData, newItem: BgEntryData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BgEntryData, newItem: BgEntryData): Boolean {
        return oldItem.title == newItem.title &&
                oldItem.id == newItem.id &&
                oldItem.teseraStringId == newItem.teseraStringId &&
                oldItem.isNew == newItem.isNew &&
                oldItem.isMine == newItem.isMine &&
                oldItem.qty == newItem.qty &&
                oldItem.date == newItem.date &&
                oldItem.weight == newItem.weight
    }
}