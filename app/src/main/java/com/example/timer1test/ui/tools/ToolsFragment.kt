package com.example.timer1test.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.timer1test.AppData
import com.example.timer1test.databinding.FragmentToolsBinding
import com.example.timer1test.model.AppMode

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
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}