package ru.sigenna.bgcounter.data

import org.junit.Assert
import org.junit.Assert.*

import org.junit.Test
import ru.sigenna.bgcounter.model.BgEntryData
import ru.sigenna.bgcounter.model.BgStatsData
import ru.sigenna.bgcounter.model.BgWeight
import java.time.Instant

class RepositoryTest {
    private var repository = Repository()
    private val entry1 = BgEntryData(
        title = "Similo",
        id = "1469924",
        teseraStringId = "Similo",
        qty = 3,
        date = Instant.parse("2025-02-11T00:00:00+03:00").toEpochMilli(),
        isNew = false,
        isMine = false,
        weight = BgWeight.EASY
    )
    private val entry2 = BgEntryData(
        title = "Сет",
        id = "4600",
        teseraStringId = "set",
        qty = 2,
        date = Instant.parse("2025-02-23T00:00:00+03:00").toEpochMilli(),
        isNew = true,
        isMine = true,
        weight = BgWeight.EASY
    )

    @Test
    fun getEntriesList() {
        val expected = MockEntriesData.list.sortedByDescending { it.date }
        val actual = repository.getEntriesList()

        assertEquals(expected, actual)
    }

    @Test
    fun addEntry() {
        repository.fillRepository(listOf(entry1))
        repository.addEntry(entry2)
        val expected = listOf(entry2, entry1)
        val actual = repository.getEntriesList()

        assertEquals(expected, actual)
    }

    @Test
    fun getStatistics() {
        val expected = BgStatsData(
            total = 9,
            totalNew = 1,
            totalUnpacked = 1,
            totalMine = 1,
            easy = 7,
            easyPercent = 78,
            middle = 1,
            middlePercent = 11,
            heavy = 1,
            heavyPercent = 11
        )

        val actual = repository.getStatistics()

        assertEquals(expected, actual)
    }

    @Test
    fun getEntryById() {
        val expected = entry1
        val actual = repository.getEntryById(expected.id)
        assertEquals(expected, actual)
    }

    @Test
    fun removeEntry() {
        repository.fillRepository(listOf(entry1, entry2))
        repository.deleteEntry(entry2)
        val expected = listOf(entry1)
        val actual = repository.getEntriesList()

        assertEquals(expected, actual)
    }
}