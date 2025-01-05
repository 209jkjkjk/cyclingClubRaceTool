package com.example.timer1test

import java.time.LocalDateTime

class RaceInfo {
    var riderList: List<Rider> = listOf()
}


class Rider(var id: Int, var name: String){
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null
}

object appData{

    var riderList: MutableList<Rider> = mutableListOf()
}