package com.apptive.japkor.ui.main.home

data class HomeMatching(
    val matchingId: Long,
    val memberId: Long,
    val name: String,
    val email: String,
    val height: Int?,
    val weight: Int?,
    val residenceArea: String?,
    val matchingOrder: Int?,
    val status: String
)
