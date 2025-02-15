package ru.sigenna.bgcounter.model

data class BgEntryData (
    val title: String,
    val id: String,
    val teseraStringId: String,
    val qty: Int,
    val date: Long,
    val isMine: Boolean,
    val isNew: Boolean,
    val weight: BgWeight?
)