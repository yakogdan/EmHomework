package com.yakogdan.emhomework.asdk.task_router

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.yakogdan.emhomework.R
import com.yakogdan.emhomework.databinding.FragmentThirdBinding

class ThirdFragment : Fragment(R.layout.fragment_third) {

    private var _binding: FragmentThirdBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentThirdBinding.bind(view)

        binding.thirdFragmentTextView.text = "ThirdFragment"
    }
}