package edu.zjut.cyclingClubRaceTool.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.zjut.cyclingClubRaceTool.AppData
import edu.zjut.cyclingClubRaceTool.R
import edu.zjut.cyclingClubRaceTool.databinding.FragmentToolsBinding
import edu.zjut.cyclingClubRaceTool.model.AppMode

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null

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

        binding.workmodeSwitch.setOnCheckedChangeListener{ _, isChecked ->
            AppData.appMode = if (isChecked) AppMode.Finnish else AppMode.Start
            binding.workmodeString.text = if (isChecked) resources.getString(R.string.workMode_Finish) else resources.getString(R.string.workMode_Start)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}