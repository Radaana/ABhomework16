package ru.sigenna.bgcounter.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import ru.sigenna.bgcounter.model.BgEntryData
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor() {
    private var list : MutableList<BgEntryData> = mutableListOf()

    fun getEntriesList(): List<BgEntryData> {
        return list.toList()
    }

    fun addEntry(entry: BgEntryData) {
        list.add(entry)
    }
}