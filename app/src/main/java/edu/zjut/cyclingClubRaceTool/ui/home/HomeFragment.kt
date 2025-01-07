package edu.zjut.cyclingClubRaceTool.ui.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.R
import edu.zjut.cyclingClubRaceTool.databinding.FragmentHomeBinding
import edu.zjut.cyclingClubRaceTool.databinding.RaceFinishListItemBinding
import edu.zjut.cyclingClubRaceTool.databinding.RaceStartListItemBinding
import edu.zjut.cyclingClubRaceTool.model.AppMode
import edu.zjut.cyclingClubRaceTool.model.Rider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("NotifyDataSetChanged")
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

        // 根据工作模式加载不同的适配器
        when(AppData.appMode){
            AppMode.Start -> {
                val adapter = RaceStartAdapter(AppData.riderList)
                binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
                binding.riderRecyclerView.adapter = adapter
                // 按钮功能
                binding.timeButton.setOnClickListener {
                    // 找到下一个未出发的选手
                    val r = AppData.getNextStartRider()
                    r?.let{
                        r.startTime = LocalDateTime.now()
                    }
                    adapter.selectedPosition = -1
                    adapter.notifyDataSetChanged()
                }
            }
            AppMode.Finnish -> {
                val adapter = RaceFinishAdapter(AppData.tempFinishList)
                binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
                binding.riderRecyclerView.adapter = adapter
                // 按钮功能
                binding.timeButton.setOnClickListener {
                    AppData.tempFinishList.add(Rider(AppData.tempFinishList.count()+1, ""))
                    AppData.tempFinishList.last().endTime = LocalDateTime.now()
                    adapter.notifyDataSetChanged()
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


// Start recyclerView
class RaceStartAdapter(val riderList: List<Rider>) : RecyclerView.Adapter<RaceStartAdapter.ItemViewHolder>(){
    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            RaceStartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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


    @SuppressLint("NotifyDataSetChanged")
    inner class ItemViewHolder(val view: RaceStartListItemBinding): RecyclerView.ViewHolder(view.root){
        init{
            itemView.setOnClickListener { // 获取当前点击的位置
                val position = adapterPosition
                // 如果已经有时间则不能选中，并删除之前的选中
                if((AppData.appMode == AppMode.Start && riderList[position].startTime != null) ||
                    (AppData.appMode == AppMode.Finnish && riderList[position].endTime != null)){
                    selectedPosition = -1
                    notifyDataSetChanged()
                    return@setOnClickListener
                }
                // 如果当前位置和之前选中的位置不同，则更新选中位置，并刷新RecyclerView
                else if (position != selectedPosition) {
                    selectedPosition = position
                    notifyDataSetChanged()
                }
            }

        }

        fun bind(rider: Rider){
            view.riderId.text = rider.id.toString()
            view.riderName.text = rider.name
            val df = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS")
            // 根据运行模式切换默认字样
            view.riderTime.text = if(rider.startTime == null) "未出发" else df.format(rider.startTime)
        }


    }
}

// Finish recyclerView
class RaceFinishAdapter(val riderList: List<Rider>) : RecyclerView.Adapter<RaceFinishAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            RaceFinishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return riderList.size
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = riderList[position]
        holder.bind(item)
    }


    inner class ItemViewHolder(val view: RaceFinishListItemBinding): RecyclerView.ViewHolder(view.root){
        init{
            itemView.setOnClickListener { // 获取当前点击的位置
//                val position = adapterPosition
//                // 如果已经有时间则不能选中，并删除之前的选中
//                if((AppData.appMode == AppMode.Start && riderList[position].startTime != null) ||
//                    (AppData.appMode == AppMode.Finnish && riderList[position].endTime != null)){
//                    selectedPosition = -1
//                    notifyDataSetChanged()
//                    return@setOnClickListener
//                }
//                // 如果当前位置和之前选中的位置不同，则更新选中位置，并刷新RecyclerView
//                else if (position != selectedPosition) {
//                    selectedPosition = position
//                    notifyDataSetChanged()
//                }
                Toast.makeText(itemView.context, "让我们说中文", Toast.LENGTH_LONG).show()
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

