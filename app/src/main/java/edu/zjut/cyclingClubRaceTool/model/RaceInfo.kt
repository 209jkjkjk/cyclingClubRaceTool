package edu.zjut.cyclingClubRaceTool.model

import java.io.Serializable
import java.time.LocalDateTime

class RaceInfo {
    var riderList: List<Rider> = listOf()
}

data class Rider(var id: Int?, var name: String): Serializable{
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null

    constructor(id: Int?, name: String, startTime: LocalDateTime?, endTime: LocalDateTime?):this(id, name){
        this.startTime = startTime
        this.endTime = endTime
    }
}