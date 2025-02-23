package ru.sigenna.bgcounter.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.sigenna.bgcounter.data.Repository
import ru.sigenna.bgcounter.model.BgEntryData
import javax.inject.Inject

class EntryListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

     val adapter: EntryListAdapter by lazy { EntryListAdapter() }
     var list: List<BgEntryData> = repository.getEntriesList()

    fun setAdapterList() {
        adapter.submitList(list)
    }
}