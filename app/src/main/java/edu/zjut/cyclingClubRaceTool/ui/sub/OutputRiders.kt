package edu.zjut.cyclingClubRaceTool.ui.sub

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityOutputRidersBinding


class OutputRiders : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOutputRidersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 以csv格式输出人员名单
        val sb = StringBuilder()
        for(rider in AppData.riderList){
            // 跳过空名或空ID的rider
            if(rider.id == null || rider.name.isEmpty()) continue
            sb.append("${rider.id},${rider.name}\n")
        }
        if(sb.isEmpty())sb.append("不存在骑手，或所有骑手的id或姓名为空")

        binding.outputRiderEditText.setText(sb.toString())

        // 复制到剪贴板按钮
        binding.CopyButton.setOnClickListener {
            //获取剪贴板管理器
            val cm: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("", binding.outputRiderEditText.text)
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            
            Toast.makeText(this, "已复制文本到剪贴板", Toast.LENGTH_SHORT).show()
        }
    }


}

