package com.yakogdan.emhomework.asdk.task_router

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yakogdan.emhomework.R
import com.yakogdan.emhomework.databinding.FragmentFirstBinding

class FirstFragment : Fragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFirstBinding.bind(view)

        binding.tvTitleFirst.text = "FirstFragment_"
    }
}