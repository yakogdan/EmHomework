package com.yakogdan.emhomework.asdk.task_work_manager

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.yakogdan.emhomework.R

class WmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wm)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkPermissionAndEnqueueWork()
    }

    private fun checkPermissionAndEnqueueWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val isGranted = ContextCompat.checkSelfPermission(
                /* context = */ this,
                /* permission = */ Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                enqueueWork()
            } else {
                requestPermissionAndEnqueueWork()
            }
        }
    }

    private fun requestPermissionAndEnqueueWork() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            registerForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                callback = { isGranted -> if (isGranted) enqueueWork() },
            ).apply {
                launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun enqueueWork() {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        val request = OneTimeWorkRequestBuilder<ChargingNotifyWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(request = request)
    }
}