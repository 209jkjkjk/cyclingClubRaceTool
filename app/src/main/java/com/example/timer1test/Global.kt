package com.example.timer1test

import com.example.timer1test.model.AppMode
import com.example.timer1test.model.Rider

// 全局数据
object AppData {
    // 工作模式
    var appMode = AppMode.Start

    var riderList: MutableList<Rider> = mutableListOf()
    init{
        for(i in 0.. 6){
            riderList.add(Rider(i, "选手$i"))
        }
    }


    var nextRider: Rider? = null
    private fun tryGetNextRider(): Rider?{
        if (nextRider != null) {
            val temp: Rider? = nextRider
            nextRider = null
            return temp
        }
        return null
    }


    // 找到下一个没有出发的选手
    fun getNextStartRider(): Rider?{
        tryGetNextRider()?.let{
            if (it.startTime == null) return it
        }
        return try {
            riderList.first{it.startTime == null}
        } catch (e:NoSuchElementException){
            null
        }
    }

    // 找到下一个没有完赛的选手
    fun getNextFinishRider(): Rider?{
        tryGetNextRider()?.let{
            if (it.endTime == null) return it
        }
        return try {
            riderList.first{it.endTime == null}
        } catch (e:NoSuchElementException){
            null
        }
    }
}