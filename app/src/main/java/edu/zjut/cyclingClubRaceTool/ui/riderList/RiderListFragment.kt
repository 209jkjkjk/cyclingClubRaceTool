package edu.zjut.cyclingClubRaceTool.ui.riderList

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.AppData.getNotNullFilteredRiderList
import edu.zjut.cyclingClubRaceTool.R
import edu.zjut.cyclingClubRaceTool.databinding.FragmentRiderListBinding
import edu.zjut.cyclingClubRaceTool.databinding.RiderListItemBinding
import edu.zjut.cyclingClubRaceTool.model.Rider
import edu.zjut.cyclingClubRaceTool.ui.sub.EditRider
import edu.zjut.cyclingClubRaceTool.ui.sub.EditStartRider
import edu.zjut.cyclingClubRaceTool.ui.sub.InputRiders
import edu.zjut.cyclingClubRaceTool.ui.sub.OutputRiders

class RiderListFragment : Fragment() {

    private var _binding: FragmentRiderListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var adapter:riderAdapter

    // 接收第二个Activity返回的回调
    @SuppressLint("NotifyDataSetChanged")
    private val requestDataLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged()
            AppData.saveDataToFile(this.requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiderListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // recyclerView
        adapter = riderAdapter()
        binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.riderRecyclerView.adapter = adapter

        // 添加按钮
        val inputId = binding.inputRiderId
        val inputName = binding.inputRiderName
        binding.addRider.setOnClickListener {
            val id = inputId.text.toString().toIntOrNull()
            if (id == null) {
                Toast.makeText(context, "id必须为整数", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (inputId.text.isNotEmpty() && inputName.text.isNotEmpty()) {
                AppData.riderList.add(
                    Rider(
                        inputId.text.toString().toInt(),
                        inputName.text.toString()
                    )
                )
                adapter.notifyDataSetChanged()
                // 清空输入
                inputId.text.clear()
                inputName.text.clear()
            }
        }

        // 导入
        binding.inputRidersButton.setOnClickListener {
            val intent =  Intent(context, InputRiders::class.java)
            requestDataLauncher.launch(intent)
        }

        // 导出
        binding.outputRidersButton.setOnClickListener {
            val intent = Intent(context, OutputRiders::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    // 下面是recyclerView代码
    inner class riderAdapter() : RecyclerView.Adapter<riderAdapter.ItemViewHolder>(){
        @SuppressLint("NotifyDataSetChanged")
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding = RiderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
            val holder = ItemViewHolder(binding)
            // 删除已按钮
            binding.deleteButton.setOnClickListener{
                val rider = AppData.riderList[holder.adapterPosition]
                if(rider.startTime != null || rider.finishTime != null){
                    Toast.makeText(parent.context, "该骑手存在成绩，删除失败", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                AppData.riderList.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//            notifyItemRangeChanged(holder.adapterPosition, itemCount)
                notifyDataSetChanged()
            }
            // 编辑按钮
            binding.editButton.setOnClickListener {
                val intent =  Intent(parent.context, EditRider::class.java)
                intent.putExtra("objectRiderIndex", holder.adapterPosition)
                editLauncher.launch(intent)
            }

            return holder
        }

        override fun getItemCount(): Int {
            return getNotNullFilteredRiderList().size
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val item = getNotNullFilteredRiderList()[position]
            holder.bind(item)
        }
        inner class ItemViewHolder(val view: RiderListItemBinding): RecyclerView.ViewHolder(view.root){
            fun bind(rider: Rider){
                view.riderId.text = itemView.context.getString(R.string.riderId, rider.id.toString())
                view.riderName.text = rider.name
            }
        }
    }
}



