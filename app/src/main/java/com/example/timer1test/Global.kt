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

    // 找到下一个没有出发的选手
    fun getNextStartRider(): Rider?{
        try {
            return riderList.first{it.startTime == null}
        } catch (e:NoSuchElementException){
            return null
        }
    }
    // 找到下一个没有完赛的选手
    fun getNextFinishRider(): Rider?{
        try {
            return riderList.first{it.startTime == null}
        } catch (e:NoSuchElementException){
            return null
        }
    }
}