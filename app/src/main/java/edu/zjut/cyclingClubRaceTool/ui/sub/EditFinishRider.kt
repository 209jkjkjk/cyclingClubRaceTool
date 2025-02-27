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


class EditFinishRider : AppCompatActivity() {
    lateinit var objectRider: Rider

    private val cloneLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 响应选择
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    @Suppress("DEPRECATION")
    private val exchangeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 响应选择
        if (result.resultCode == RESULT_OK) {
            result.data?.let{
                val exchangeRider = it.getSerializableExtra("exchangeRider") as Rider
                objectRider.endTime = exchangeRider.endTime
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
        objectRider = AppData.getFinishFilteredRiderList()[riderIndex]
        Log.d("zjut", "编辑的rider $objectRider")

        // 复制
        binding.cloneButton.setOnClickListener {
            val intent =  Intent(this, ChooseCloneFinishRider::class.java)
            intent.putExtra("tempRider", objectRider)
            cloneLauncher.launch(intent)
        }

        // 交换
        binding.exchangeButton.setOnClickListener {
            val intent =  Intent(this, ChooseExchangeFinishRider::class.java)
            intent.putExtra("tempRider", objectRider)
            exchangeLauncher.launch(intent)
        }

        // 删除
        binding.clearButton.setOnClickListener {
            Toast.makeText(this, "长按删除", Toast.LENGTH_SHORT).show()
        }
        binding.clearButton.setOnLongClickListener {
            objectRider.endTime = null
            Toast.makeText(this, "到达成绩已清空", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            return@setOnLongClickListener true
        }
    }


}

