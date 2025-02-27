package edu.zjut.cyclingClubRaceTool.ui.sub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.databinding.ActivityChooseFinishRiderBinding
import edu.zjut.cyclingClubRaceTool.databinding.ChooseListItemBinding
import edu.zjut.cyclingClubRaceTool.model.Rider

class ChooseCloneStartRider : AppCompatActivity() {
    lateinit var tempRider: Rider

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChooseFinishRiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 获取传送而来的数据
        tempRider = intent.getSerializableExtra("tempRider") as Rider

        val adapter = RiderListAdapter()
        binding.riderRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.riderRecyclerView.adapter = adapter
    }

    inner class RiderListAdapter() : RecyclerView.Adapter<RiderListAdapter.ItemViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding =
                ChooseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val holder = ItemViewHolder(binding)

            binding.confirmButton.setOnClickListener{
                val resultRider = AppData.getNotNullFilteredRiderList()[holder.adapterPosition]
                resultRider.startTime = tempRider.startTime
                setResult(RESULT_OK)
                finish()
            }
            return holder
        }

        override fun getItemCount(): Int {
            return AppData.getNotNullFilteredRiderList().size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = AppData.getNotNullFilteredRiderList()[position]
            holder.bind(item)
        }

        @SuppressLint("NotifyDataSetChanged")
        inner class ItemViewHolder(val view: ChooseListItemBinding): RecyclerView.ViewHolder(view.root){
            fun bind(rider: Rider){
                view.riderId.text = rider.id.toString()
                view.riderName.text = rider.name
            }
        }
    }
}

