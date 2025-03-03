package edu.zjut.cyclingClubRaceTool

import android.content.Context
import android.util.Log
import edu.zjut.cyclingClubRaceTool.model.AppMode
import edu.zjut.cyclingClubRaceTool.model.Rider
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 全局数据
object AppData {
    val df = DateTimeFormatter.ofPattern("yyyyMMdd_HH:mm:ss.SSSS")
    // 工作模式
    var appMode = AppMode.Finnish
    // 选手列表
    var riderList: MutableList<Rider> = mutableListOf()

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
            if (it.finishTime == null) return it
        }
        return try {
            riderList.first{it.finishTime == null}
        } catch (e:NoSuchElementException){
            null
        }
    }

    fun getFinishFilteredRiderList(): List<Rider>{
        return riderList.filter{it.finishTime != null}
    }

    fun getStartFilteredRiderList(): List<Rider>{
        return riderList.filter{it.finishTime == null}
    }

    // 获得id不为null的骑手列表
    fun getNotNullFilteredRiderList(): List<Rider>{
        return riderList.filter{it.id != null}
    }

    fun saveDataToFile(context:Context){
        try{
            val output = context.openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            Log.d("zjut", "开始保存文件")
            writer.use{
                it.write(if(appMode == AppMode.Start) "0" else "1")
                it.newLine()
                it.write(riderList.count().toString())
                Log.d("zjut", "数量" + riderList.count())
                it.newLine()
                for(r in riderList){
                    if(r.id == null) it.write("null") else it.write(r.id.toString())
                    it.newLine()
                    it.write(r.name)
                    it.newLine()
                    if(r.startTime == null) it.write("null") else it.write(df.format(r.startTime))
                    it.newLine()
                    if(r.finishTime == null) it.write("null") else it.write(df.format(r.finishTime))
                    it.newLine()
                }
            }
            writer.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun loadDataFromFile(context: Context){
        try{
            val input = context.openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            Log.d("zjut", "开始读取文件")
            reader.use{
                val mode = it.readLine()
                appMode = if(mode == "0") AppMode.Start else AppMode.Finnish
                val listLength = it.readLine().toInt()
                for(r in 0 until listLength){
                    val idstr = it.readLine()
                    val id = if(idstr == "null") null else idstr.toInt()
                    val name = it.readLine()
                    val startTimeStr = it.readLine()
                    val startTime:LocalDateTime? = if(startTimeStr == "null") null else LocalDateTime.parse(startTimeStr, df)
                    val endTimeStr = it.readLine()
                    val endTime:LocalDateTime? = if(endTimeStr == "null") null else LocalDateTime.parse(endTimeStr, df)
                    riderList.add(Rider(id, name, startTime, endTime))
                }
            }
            Log.d("zjut", "数量${riderList.count()}")
        }catch (e :IOException){
            e.printStackTrace()
        }
    }

}