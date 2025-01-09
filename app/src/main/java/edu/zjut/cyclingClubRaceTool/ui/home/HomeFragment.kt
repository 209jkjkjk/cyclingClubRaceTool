package edu.zjut.cyclingClubRaceTool.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.AppData.getFinishFilteredRiderList
import edu.zjut.cyclingClubRaceTool.AppData.getNotNullFilteredRiderList
import edu.zjut.cyclingClubRaceTool.R
import edu.zjut.cyclingClubRaceTool.databinding.FragmentHomeBinding
import edu.zjut.cyclingClubRaceTool.databinding.RaceFinishListItemBinding
import edu.zjut.cyclingClubRaceTool.databinding.RaceStartListItemBinding
import edu.zjut.cyclingClubRaceTool.model.AppMode
import edu.zjut.cyclingClubRaceTool.model.Rider
import edu.zjut.cyclingClubRaceTool.ui.sub.ChooseFinishRider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {
    var finishAdapter: RaceFinishAdapter? = null
    @SuppressLint("NotifyDataSetChanged")
    private val requestDataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // 响应选择
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            finishAdapter?.notifyDataSetChanged()
        }
    }

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
                val adapter = RaceStartAdapter()
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
                finishAdapter = RaceFinishAdapter()
                binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
                binding.riderRecyclerView.adapter = finishAdapter
                // 按钮功能
                binding.timeButton.setOnClickListener {
                    val tempRider = Rider(null, "")
                    AppData.riderList.add(tempRider)
                    tempRider.endTime = LocalDateTime.now()
                    finishAdapter!!.notifyDataSetChanged()
                }
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // Start recyclerView
    inner class RaceStartAdapter() : RecyclerView.Adapter<RaceStartAdapter.ItemViewHolder>(){
        var selectedPosition: Int = -1

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding =
                RaceStartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return getNotNullFilteredRiderList().size
        }


        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = getNotNullFilteredRiderList()[position]
            holder.bind(item)
            if (selectedPosition == position) {
                AppData.nextRider = getNotNullFilteredRiderList()[position]
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
                    if((AppData.appMode == AppMode.Start && getNotNullFilteredRiderList()[position].startTime != null) ||
                        (AppData.appMode == AppMode.Finnish && getNotNullFilteredRiderList()[position].endTime != null)){
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
    inner class RaceFinishAdapter() : RecyclerView.Adapter<RaceFinishAdapter.ItemViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding =
                RaceFinishListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
            return getFinishFilteredRiderList().size
        }


        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = getFinishFilteredRiderList()[position]
            holder.bind(item)
        }


        @SuppressLint("NotifyDataSetChanged")
        inner class ItemViewHolder(val view: RaceFinishListItemBinding): RecyclerView.ViewHolder(view.root){
            init{
                itemView.setOnClickListener { // 获取当前点击的位置
                    val intent =  Intent(itemView.context, ChooseFinishRider::class.java)
                    val rider = getFinishFilteredRiderList()[adapterPosition]
                    // 只处理id为null的
                    if(rider.id == null){
                        intent.putExtra("tempRider", rider)
                        requestDataLauncher.launch(intent)
                    }
                }
            }

            fun bind(rider: Rider){
                view.riderId.text = if(rider.id == null) "___" else rider.id.toString()
                view.riderName.text = rider.name
                val df = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS")
                // 根据运行模式切换默认字样
                view.riderTime.text = if(rider.endTime == null) "未到达" else df.format(rider.endTime)
            }
        }
    }
}
