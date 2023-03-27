package com.capstone.yojo.model

data class MarkerData(
    var key: String = "",
    var address: String = "",
    var details: List<MarkerDetails> = listOf(),
    var build: Long = 0,
    var dong: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var name: String = "",
)