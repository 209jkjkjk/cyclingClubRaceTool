package com.example.timer1test.ui.riderList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timer1test.AppData
import com.example.timer1test.model.Rider
import com.example.timer1test.databinding.FragmentRiderListBinding
import com.example.timer1test.databinding.RiderListItemBinding

class RiderListFragment : Fragment() {

    private var _binding: FragmentRiderListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRiderListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // recyclerView
        val adapter = riderAdapter(AppData.riderList)
        binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.riderRecyclerView.adapter = adapter

        // 添加按钮
        val inputId = binding.inputRiderId
        val inputName = binding.inputRiderName
        binding.addRider.setOnClickListener {
            if(inputId.text.isNotEmpty() && inputName.text.isNotEmpty()){
                AppData.riderList.add(Rider(inputId.text.toString().toInt(), inputName.text.toString()))
                adapter.notifyItemInserted(AppData.riderList.size-1)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 下面是recyclerView代码
class riderAdapter(val itemList: List<Rider>) : RecyclerView.Adapter<ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RiderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        val holder = ItemViewHolder(binding)
        binding.delete.setOnClickListener{
            Log.d("lai", "lai")
            AppData.riderList.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//            notifyItemRangeChanged(holder.adapterPosition, itemCount)
            notifyDataSetChanged()
        }
        return holder
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var item = itemList[position]
        holder.bind(item)
    }

}

class ItemViewHolder(val view: RiderListItemBinding): RecyclerView.ViewHolder(view.root){
    fun bind(rider: Rider){
        view.riderId.text = "#${rider.id}"
        view.riderName.text = rider.name
    }
}


