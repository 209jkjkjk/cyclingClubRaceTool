package edu.zjut.cyclingClubRaceTool.model

import java.io.Serializable
import java.time.LocalDateTime

data class Rider(var id: Int?, var name: String): Serializable{
    var startTime: LocalDateTime? = null
    var endTime: LocalDateTime? = null

    constructor(id: Int?, name: String, startTime: LocalDateTime?, endTime: LocalDateTime?):this(id, name){
        this.startTime = startTime
        this.endTime = endTime
    }

    constructor(rider: Rider):this(rider.id, rider.name){
        this.startTime = rider.startTime
        this.endTime = rider.endTime
    }
}