package ru.sigenna.bgcounter.model

data class BgStatsData (
    val total: Int,
    val totalNew: Int,
    val totalUnpacked: Int,
    val totalMine: Int,
    val easy: Int,
    val easyPercent: Int,
    val middle: Int,
    val middlePercent: Int,
    val heavy: Int,
    val heavyPercent: Int
)