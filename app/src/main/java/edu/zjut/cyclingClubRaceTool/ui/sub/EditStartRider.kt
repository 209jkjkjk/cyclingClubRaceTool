package edu.zjut.cyclingClubRaceTool.ui.sub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityEditStartRiderBinding
import edu.zjut.cyclingClubRaceTool.model.Rider


class EditStartRider : AppCompatActivity() {
    lateinit var objectRider: Rider

    @Suppress("DEPRECATION")
    private val exchangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 响应选择
        if (result.resultCode == RESULT_OK) {
            result.data?.let{
                val exchangeRider = it.getSerializableExtra("exchangeRider") as Rider
                objectRider.startTime = exchangeRider.startTime
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditStartRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val riderIndex = intent.getSerializableExtra("objectRiderIndex") as Int
        objectRider = AppData.getNotNullFilteredRiderList()[riderIndex]
        Log.d("zjut", "编辑的rider $objectRider")

        // 复制

        // 交换
        binding.exchangeButton.setOnClickListener {
            val intent =  Intent(this, ChooseExchangeStartRider::class.java)
            intent.putExtra("tempRider", objectRider)
            exchangeLauncher.launch(intent)
        }

        // 删除
        binding.clearButton.setOnClickListener {
            Toast.makeText(this, "长按删除", Toast.LENGTH_SHORT).show()
        }
        binding.clearButton.setOnLongClickListener {
            objectRider.startTime = null
            Toast.makeText(this, "出发成绩已清空", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            return@setOnLongClickListener true
        }
    }


}

