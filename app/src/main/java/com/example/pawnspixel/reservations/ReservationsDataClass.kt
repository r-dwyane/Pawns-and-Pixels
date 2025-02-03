package com.example.pawnspixel.reservations

data class ReservationsDataClass(
    val reservationId: String = "",
    val status: String = "",
    val room: String = "",
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val numberOfPlayers: Int = 0,
    val createdAt: String = ""
)
