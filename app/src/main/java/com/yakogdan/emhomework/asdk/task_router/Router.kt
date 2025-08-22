package com.yakogdan.emhomework.asdk.task_router

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Router(
    val fragmentManager: FragmentManager,
    val containerId: Int,
) {

    fun newRootScreen(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    fun navigateTo(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(containerId, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    fun back() {
        fragmentManager.popBackStack()
    }
}

interface RouterProvider {
    val router: Router
}

val Fragment.router: Router
    get() = (requireActivity() as RouterProvider).router