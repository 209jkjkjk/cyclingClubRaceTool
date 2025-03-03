package edu.zjut.cyclingClubRaceTool.model

import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime

data class Rider(var id: Int?, var name: String): Serializable{
    /* 成员变量 */
    // 出发时间
    var startTime: LocalDateTime? = null
    // 到达时间
    var finishTime: LocalDateTime? = null
    // 减秒奖励
    var timeBonus: Duration? = null
    // 注释
    var note: String? = null

    val duration: Duration?
        get() {
        if(startTime == null || finishTime == null) return null
        return Duration.between(startTime, finishTime)
    }

    val isDNS: Boolean
        get() = startTime == null && finishTime != null

    val isDNF: Boolean
        get() = startTime != null && finishTime == null

    constructor(id: Int?, name: String, startTime: LocalDateTime?, endTime: LocalDateTime?):this(id, name){
        this.startTime = startTime
        this.finishTime = endTime
    }

    constructor(id: Int?, name: String, startTime: LocalDateTime?, endTime: LocalDateTime?, timeBonus:Duration?, note:String?):this(id, name){
        this.startTime = startTime
        this.finishTime = endTime
        this.timeBonus = timeBonus
        this.note = note
    }

    constructor(rider: Rider):this(rider.id, rider.name){
        this.startTime = rider.startTime
        this.finishTime = rider.finishTime
    }
}