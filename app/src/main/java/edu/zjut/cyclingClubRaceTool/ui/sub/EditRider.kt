package edu.zjut.cyclingClubRaceTool.ui.sub

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityEditRiderBinding
import edu.zjut.cyclingClubRaceTool.model.Rider
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EditRider : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 获取传送而来的数据
        val riderIndex = intent.getSerializableExtra("objectRiderIndex") as Int
        val objectRider = AppData.getNotNullFilteredRiderList()[riderIndex]

        // 日期格式化
        val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss.SSSS") // 定义格式
        // 填充字段
        binding.idEditText.setText(objectRider.id.toString())
        binding.nameEditText.setText(objectRider.name)
        binding.startEditText.setText( if(objectRider.startTime == null) "" else objectRider.startTime!!.format(formatter))
        binding.finishEditText.setText( if(objectRider.finishTime == null) "" else objectRider.finishTime!!.format(formatter))
        binding.timeBonusEditText.setText( if(objectRider.timeBonus == null) "" else objectRider.timeBonus!!.seconds.toString())
        binding.noteEditText.setText(objectRider.note)

        binding.confirmButton.setOnClickListener {
            try{
                // 首先检查字段合法性
                val id = binding.idEditText.text.toString().toInt()
                val name = binding.nameEditText.text.toString()
                val startTime = if(binding.startEditText.text.isEmpty()) null else LocalDateTime.parse(binding.startEditText.text.toString(), formatter)
                val finishTime = if(binding.finishEditText.text.isEmpty()) null else LocalDateTime.parse(binding.finishEditText.text.toString(), formatter)
                val timeBonus = if(binding.timeBonusEditText.text.isEmpty()) null else Duration.ofSeconds( binding.timeBonusEditText.text.toString().toLong())
                val note = if(binding.noteEditText.text.isEmpty()) null else binding.noteEditText.text.toString()

                objectRider.id = id
                objectRider.name = name
                objectRider.startTime = startTime
                objectRider.finishTime = finishTime
                objectRider.timeBonus = timeBonus
                objectRider.note = note

                setResult(RESULT_OK)
                finish()
            }catch (e:Exception){
                Log.d("zjut", e.message?:"")
                Toast.makeText(this, ("修改失败" + e.message), Toast.LENGTH_LONG).show()
            }
        }

    }
}

