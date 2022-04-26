package com.example.tipsaredone.model

import java.util.*

data class TipReport(
    val dateStart: String,
    val dateEnd: String,
    val tipsToCollect: Int,
    val tipsCollected: Boolean = false
)
