package com.example.jobstest

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.jobstest.databinding.ActivityMainBinding
import com.example.jobstest.screens.Favorites
import com.example.jobstest.screens.Search
import com.example.jobstest.viewmodel.JobsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // Получаем ViewModel для теста через koin
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


        binding?.bottomNav?.setOnItemSelectedListener {item ->
            when(item.itemId){
                R.id.searchFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Search()).commit()
                R.id.favoritesFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Favorites()).commit()
              //  R.id.taskItemBottomNav -> supportFragmentManager.beginTransaction().replace(R.id.content, TaskForType()).commit()
            }
            return@setOnItemSelectedListener true
        }


    }

}