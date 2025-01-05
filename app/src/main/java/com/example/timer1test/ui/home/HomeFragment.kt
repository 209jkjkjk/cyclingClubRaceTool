package com.example.timer1test.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timer1test.AppData
import com.example.timer1test.R
import com.example.timer1test.databinding.FragmentHomeBinding
import com.example.timer1test.databinding.RaceListItemBinding
import com.example.timer1test.model.AppMode
import com.example.timer1test.model.Rider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // 根据工作模式替换字样
        binding.timeButton.text = when(AppData.appMode){
            AppMode.Start -> resources.getString(R.string.startButtonText)
            AppMode.Finnish -> resources.getString(R.string.finishButtonText)
        }

        // 注册recyclerView
        val adapter = RaceAdapter(AppData.riderList)
        binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.riderRecyclerView.adapter = adapter

        // 按钮功能
        binding.timeButton.setOnClickListener {
            when(AppData.appMode){
                AppMode.Start -> {
                    // 找到下一个未出发的选手
                    val r = AppData.getNextStartRider()
                    r?.let{
                        r.startTime = LocalDateTime.now()
                    }
                }
                AppMode.Finnish ->{
                    // 找到下一个未到达的选手
                    val r = AppData.getNextFinishRider()
                    r?.let{
                        r.endTime = LocalDateTime.now()
                    }
                }
            }
            adapter.selectedPosition = -1
            adapter.notifyDataSetChanged()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


// 下面是recyclerView代码
class RaceAdapter(val riderList: List<Rider>) : RecyclerView.Adapter<RaceAdapter.ItemViewHolder>(){
    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            RaceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return riderList.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = riderList[position]
        holder.bind(item)
        if (selectedPosition == position) {
            AppData.nextRider = riderList[position]
            holder.itemView.setBackgroundColor(Color.parseColor("#FFA1C3"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }


    inner class ItemViewHolder(val view: RaceListItemBinding): RecyclerView.ViewHolder(view.root){
        init{
            itemView.setOnClickListener { // 获取当前点击的位置
                val position = getAdapterPosition()
                // 如果当前位置和之前选中的位置不同，则更新选中位置，并刷新RecyclerView
                if (position != selectedPosition) {
                    selectedPosition = position
                    notifyDataSetChanged()
                }
            }

        }

        fun bind(rider: Rider){
            view.riderId.text = "#${rider.id}"
            view.riderName.text = rider.name
            val df = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS")
            // 根据运行模式切换默认字样
            view.riderTime.text = when(AppData.appMode){
                AppMode.Start -> {
                    if(rider.startTime == null) "未出发"
                    else df.format(rider.startTime)
                }
                AppMode.Finnish -> {
                    if(rider.endTime == null) "未到达"
                    else df.format(rider.endTime)
                }
            }
        }


    }
}


