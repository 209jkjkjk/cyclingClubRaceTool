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
            val text = binding.inputRiderEditText.text
            val tempList: MutableList<Rider> = mutableListOf()

            try{
                // 解析字符串 收集导入的数据
                val riderStrings = text.split(Regex("\r\n|(?<!\r\n)\n(?!\r\n)|(?<!\r\n|\n)\r(?!\r\n|\n)"))  // 换行作为分隔符
                for(riderString in riderStrings){
                    // 过滤空行
                    if(riderString.isEmpty()) continue

                    val temp = riderString.split(",")   // 逗号作为分隔符
                    val id = temp[0].toInt()
                    val name = temp[1]

                    // 检查是否有重复并且有数据的，则采用原来的
                    val findResult = AppData.riderList.filter { it.name == name && it.id == id && (it.endTime != null || it.startTime != null) }
                    if(findResult.isNotEmpty()){
                        tempList.addAll(findResult)
                        continue
                    }

                    // 否则添加新的
                    tempList.add(Rider(id, name, null, null))
                }

                // 列表覆盖
                AppData.riderList = tempList
                // 成功
                setResult(RESULT_OK)
                Toast.makeText(this, "导入成功！", Toast.LENGTH_SHORT).show()
                finish()
            }catch (e:Exception){
                Log.d("zjut", e.message?:"")
                Toast.makeText(this, ("导入失败" + e.message), Toast.LENGTH_LONG).show()
            }
        }

    }


}

