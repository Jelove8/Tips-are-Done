package com.example.tipsaredone.model

import kotlinx.serialization.Serializable

data class IndividualReport (
    val id: String,
    var name: String,
    var reportID: String,
    var startDate: String,
    var endDate: String,
    var hours: Double,
    var tips: Double? = null,
    var error: Int? = null,
    var collected: Boolean = false
)



