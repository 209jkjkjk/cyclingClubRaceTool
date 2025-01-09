package edu.zjut.cyclingClubRaceTool.ui.riderList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.AppData.getNotNullFilteredRiderList
import edu.zjut.cyclingClubRaceTool.databinding.FragmentRiderListBinding
import edu.zjut.cyclingClubRaceTool.databinding.RiderListItemBinding
import edu.zjut.cyclingClubRaceTool.model.Rider

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
        val adapter = riderAdapter()
        binding.riderRecyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.riderRecyclerView.adapter = adapter

        // 添加按钮
        val inputId = binding.inputRiderId
        val inputName = binding.inputRiderName
        binding.addRider.setOnClickListener {
            val id = inputId.text.toString().toIntOrNull()
            if (id == null)
                Toast.makeText(context, "id必须为数字", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(inputId.text.isNotEmpty() && inputName.text.isNotEmpty()){
                AppData.riderList.add(Rider(inputId.text.toString().toInt(), inputName.text.toString()))
                adapter.notifyItemInserted(AppData.riderList.size-1)
            }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 下面是recyclerView代码
class riderAdapter() : RecyclerView.Adapter<riderAdapter.ItemViewHolder>(){
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = RiderListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false )
        val holder = ItemViewHolder(binding)
        binding.delete.setOnClickListener{
            AppData.riderList.removeAt(holder.adapterPosition)
//            notifyItemRemoved(holder.adapterPosition)
//            notifyItemRangeChanged(holder.adapterPosition, itemCount)
              notifyDataSetChanged()
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
    class ItemViewHolder(val view: RiderListItemBinding): RecyclerView.ViewHolder(view.root){
        fun bind(rider: Rider){
            view.riderId.text = "#${rider.id}"
            view.riderName.text = rider.name
        }
    }

}



