package edu.zjut.cyclingClubRaceTool.ui.sub

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityEditStartRiderBinding


class EditStartRider : AppCompatActivity() {

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityEditStartRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val riderIndex = intent.getSerializableExtra("objectRiderIndex") as Int
        val rider = AppData.getNotNullFilteredRiderList()[riderIndex]
        Log.d("zjut", "编辑的rider $rider")

        binding.clearButton.setOnClickListener {
            Toast.makeText(this, "长按删除", Toast.LENGTH_SHORT).show()
        }
        binding.clearButton.setOnLongClickListener {
            rider.startTime = null
            Toast.makeText(this, "出发成绩已清空", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
            return@setOnLongClickListener true
        }
    }


}

