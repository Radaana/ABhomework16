package ru.sigenna.bgcounter.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import org.jetbrains.annotations.TestOnly
import ru.sigenna.bgcounter.model.BgEntryData
import ru.sigenna.bgcounter.model.BgStatsData
import ru.sigenna.bgcounter.model.BgWeight
import javax.inject.Inject
import kotlin.math.roundToInt

@ActivityRetainedScoped
class Repository @Inject constructor() {
    private var list: MutableList<BgEntryData> = MockEntriesData.list.toMutableList()
    private var selectedEntryId: String? = null
    private var TAG = "Repository"

    @TestOnly
    fun fillRepository(newList: List<BgEntryData>) {
        list = newList.toMutableList()
    }

    fun getEntriesList(): List<BgEntryData> {
        return list.toList().sortedByDescending { it.date }
    }

    fun addEntry(entry: BgEntryData) {
        list.add(entry)
    }

    fun setSelectedEntry(id: String?) {
        selectedEntryId = id
    }

    fun getSelectedEntry(): BgEntryData? {
        if (selectedEntryId == null) {
            return  null
        } else {
            val entry = getEntryById(selectedEntryId!!)
            selectedEntryId = null
            return entry
        }
    }

    private fun getPercent(part: Int, total: Int): Int {
       return ((part.toFloat() / total.toFloat()) * 100).roundToInt()
    }

    fun getStatistics(): BgStatsData {
        var total = 0
        var totalNew = 0
        var totalUnpacked = 0
        var totalMine = 0
        var easy = 0
        var middle = 0
        var heavy = 0

        list.forEach {
            total += it.qty
            totalNew += (if (it.isNew && !it.isMine) 1 else 0)
            totalUnpacked += (if (it.isNew && it.isMine) 1 else 0)
            totalMine += (if (!it.isNew && it.isMine) 1 else 0)

            when (it.weight) {
                BgWeight.EASY -> easy += it.qty
                BgWeight.MIDDLE -> middle += it.qty
                BgWeight.HEAVY -> heavy += it.qty
                null -> {}
            }
        }

        return BgStatsData(
            total,
            totalNew,
            totalUnpacked,
            totalMine,
            easy,
            getPercent(easy, total),
            middle,
            getPercent(middle, total),
            heavy,
            getPercent(heavy, total),
        )
    }

    fun getEntryById(id: String):BgEntryData? {
        return list.find { it.id === id }
    }

    fun deleteEntry(entryId: String) {
        val entryIndex = list.indexOfFirst { it.id === entryId }
        if (entryIndex >= 0) {
            list.removeAt(entryIndex)
        }
    }

    fun editEntry(entry: BgEntryData) {
        deleteEntry(entry.id)
        addEntry(entry)
    }
}