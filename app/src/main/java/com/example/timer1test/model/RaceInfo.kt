package com.example.timer1test.model

import java.time.LocalDateTime

class RaceInfo {
    var riderList: List<Rider> = listOf()
}


class Rider(var id: Int, var name: String){
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
}