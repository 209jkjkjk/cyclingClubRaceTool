package edu.zjut.cyclingClubRaceTool.model

import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime

data class Rider(var id: Int?, var name: String): Serializable{
    // 出发时间
    var startTime: LocalDateTime? = null
    var finishTime: LocalDateTime? = null
    // 减秒奖励
    var timeBonus: Duration? = null
    // 注释
    var note: String? = null

    constructor(id: Int?, name: String, startTime: LocalDateTime?, endTime: LocalDateTime?):this(id, name){
        this.startTime = startTime
        this.finishTime = endTime
    }

    constructor(rider: Rider):this(rider.id, rider.name){
        this.startTime = rider.startTime
        this.finishTime = rider.finishTime
    }
}