package ru.sigenna.bgcounter.presentation

import androidx.lifecycle.ViewModel
import ru.sigenna.bgcounter.data.Repository
import ru.sigenna.bgcounter.model.BgEntryData
import javax.inject.Inject

class EntryListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
     var list: List<BgEntryData> = repository.getEntriesList()

     fun selectEntry(id: String) {
          repository.setSelectedEntry(id)
     }
}