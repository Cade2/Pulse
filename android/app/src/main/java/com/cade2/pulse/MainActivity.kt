package com.cade2.pulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.cade2.pulse.ui.navigation.PulseNavGraph
import com.cade2.pulse.ui.theme.PulseTheme
import com.cade2.pulse.workers.SyncWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        scheduleSyncWork()
        setContent {
            PulseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    PulseNavGraph(navController = navController)
                }
            }
        }
    }

    private fun scheduleSyncWork() {
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "pulse_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            SyncWorker.buildRequest()
        )
    }
}
