package com.yakogdan.emhomework.asdk.task_router

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yakogdan.emhomework.R
import com.yakogdan.emhomework.databinding.FragmentSecondBinding

class SecondFragment : Fragment(R.layout.fragment_second) {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSecondBinding.bind(view)

        binding.secondFragmentTextView.text = "SecondFragment"
    }
}