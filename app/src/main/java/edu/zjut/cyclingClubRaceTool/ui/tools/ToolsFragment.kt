package edu.zjut.cyclingClubRaceTool.ui.tools

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.AppData.riderList
import edu.zjut.cyclingClubRaceTool.R
import edu.zjut.cyclingClubRaceTool.databinding.FragmentToolsBinding
import edu.zjut.cyclingClubRaceTool.model.AppMode

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private var deleteCount = 5
    private var deleteToast: Toast? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // 同步开关状态，由于本项目没有用数据绑定，需要手动实现
        binding.workmodeSwitch.isChecked = AppData.appMode == AppMode.Finnish
        binding.workmodeString.text = if (binding.workmodeSwitch.isChecked) resources.getString(R.string.workMode_Finish) else resources.getString(R.string.workMode_Start)
        // 开关
        binding.workmodeSwitch.setOnCheckedChangeListener{ _, isChecked ->
            AppData.appMode = if (isChecked) AppMode.Finnish else AppMode.Start
            binding.workmodeString.text = if (isChecked) resources.getString(R.string.workMode_Finish) else resources.getString(R.string.workMode_Start)
        }

        // 删除数据
        binding.clearData.setOnClickListener {
            deleteCount--
            if(deleteCount > 0){
                deleteToast?.cancel()
                deleteToast = Toast(context);
                with(deleteToast!!) {
                    setText("再按 $deleteCount 次删除数据");
                    setDuration(Toast.LENGTH_SHORT);
                    setGravity(Gravity.CENTER, 0, 0);
                    show();
                }
            }
            else {
                riderList.clear()
                Toast.makeText(context, "数据已清空", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}