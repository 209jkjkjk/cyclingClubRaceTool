package edu.zjut.cyclingClubRaceTool.ui.sub

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityInputRidersBinding
import edu.zjut.cyclingClubRaceTool.model.Rider
import java.lang.Exception

class InputRiders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInputRidersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirmButton.setOnClickListener {
            val text = binding.inputRiderTextView.text
            val tempList: MutableList<Rider> = mutableListOf()

            try{
                // 解析字符串 收集导入的数据
                val riderStrings = text.split(Regex("\r\n|(?<!\r\n)\n(?!\r\n)|(?<!\r\n|\n)\r(?!\r\n|\n)"))  // 换行作为分隔符
                for(riderString in riderStrings){
                    val temp = riderString.split(",")   // 逗号作为分隔符
                    val id = temp[0].toInt()
                    val name = temp[1]
                    tempList.add(Rider(id, name, null, null))
                }
                // 对于原来已经有成绩记录的数据，我们要保留
                for(rider in AppData.riderList)
                    if(rider.endTime != null || rider.startTime != null)
                        tempList.add(rider)
                // 列表覆盖
                AppData.riderList = tempList
                // 成功
                setResult(RESULT_OK)
                Toast.makeText(this, "导入成功！", Toast.LENGTH_LONG).show()
                finish()
            }catch (e:Exception){
                Log.d("zjut", e.message?:"")
                Toast.makeText(this, ("导入失败" + e.message), Toast.LENGTH_LONG).show()
            }
        }

    }


}

