package com.example.pawnspixel.admin.reservations

data class AdminReservationsDataClass(
    val reservationId: String = "",
    val status: String = "",
    val room: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val players: String = "",
    val createdAt: String = "",
    var customerName: String = "",
    var userId: String? = null
) {}

