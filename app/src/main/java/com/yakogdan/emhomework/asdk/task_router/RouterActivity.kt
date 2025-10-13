package com.yakogdan.emhomework.asdk.task_router

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yakogdan.emhomework.databinding.ActivityRouterBinding

class RouterActivity : AppCompatActivity(), RouterProvider {

    private var _binding: ActivityRouterBinding? = null
    private val binding get() = _binding!!

    override val router by lazy {
        Router(
            fragmentManager = supportFragmentManager,
            containerId = binding.FragmentContainerView.id,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRouterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            router.newRootScreen(FirstFragment())
        }
    }
}