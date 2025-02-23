package ru.sigenna.bgcounter.data

import ru.sigenna.bgcounter.model.BgEntryData
import ru.sigenna.bgcounter.model.BgWeight
import java.time.Instant

object MockEntriesData {
    var list = listOf(
        BgEntryData(
            title = "Fallout. Настольная игра",
            id = "1063856",
            teseraStringId = "fallout-boardgame",
            qty = 1,
            date = Instant.parse("2025-01-09T00:00:00+03:00").toEpochMilli(),
            isNew = false,
            isMine = true,
            weight = BgWeight.HEAVY
        ),
        BgEntryData(
            title = "Волшебные королевства",
            id = "1030209",
            teseraStringId = "fantasy-realms",
            qty = 2,
            date = Instant.parse("2025-02-11T00:00:00+03:00").toEpochMilli(),
            isNew = false,
            isMine = false,
            weight = BgWeight.EASY
        ),
        BgEntryData(
            title = "Similo",
            id = "1469924",
            teseraStringId = "Similo",
            qty = 3,
            date = Instant.parse("2025-02-11T00:00:00+03:00").toEpochMilli(),
            isNew = false,
            isMine = false,
            weight = BgWeight.EASY
        ),
        BgEntryData(
            title = "Холст",
            id = "1664797",
            teseraStringId = "canvas",
            qty = 1,
            date = Instant.parse("2025-02-09T00:00:00+03:00").toEpochMilli(),
            isNew = true,
            isMine = false,
            weight = BgWeight.MIDDLE
        ),
        BgEntryData(
            title = "Сет",
            id = "4600",
            teseraStringId = "set",
            qty = 2,
            date = Instant.parse("2025-02-09T00:00:00+03:00").toEpochMilli(),
            isNew = true,
            isMine = true,
            weight = BgWeight.EASY
        )
    )
}