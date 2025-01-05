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
    init{
        for(i in 0.. 6){
            riderList.add(Rider(i, "选手$i"))
        }

    }

    fun getFirstRider(): Rider?{
        try {
            return riderList.first{it.startTime == null}
        } catch (e:NoSuchElementException){
            return null
        }
    }
}