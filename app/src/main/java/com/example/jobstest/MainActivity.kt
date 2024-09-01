package com.example.jobstest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jobstest.databinding.ActivityMainBinding
import com.example.jobstest.screens.Favorites
import com.example.jobstest.screens.Search
import com.example.jobstest.viewmodel.JobsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    private val jobsViewModel: JobsViewModel by viewModel()

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Инициализация загрузки данных
        lifecycleScope.launch {
            jobsViewModel.fetchData()
        }




        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding?.bottomNav?.setupWithNavController(navController)

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.loginFirstFragment, R.id.loginSecond -> {
//                    binding?.bottomNav?.menu?.forEach { it.isEnabled = false }
//                }
//                else -> {
//                    binding?.bottomNav?.menu?.forEach { it.isEnabled = true }
//                }
//            }
//        }
    }
}