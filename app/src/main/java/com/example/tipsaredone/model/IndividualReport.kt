package com.example.tipsaredone.model

import kotlinx.serialization.Serializable

data class IndividualReport (
    var name: String,
    val id: String,
    var hours: Double? = null,
    var distributedTips: Double? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var majorRoundingError: Int? = null,
    var collected: Boolean = false
)



