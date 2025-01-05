package com.example.timer1test

import com.example.timer1test.model.Rider

// 全局数据
object AppData {
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