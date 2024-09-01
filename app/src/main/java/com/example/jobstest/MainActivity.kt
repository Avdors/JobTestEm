package com.example.jobstest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jobstest.databinding.ActivityMainBinding
import com.example.jobstest.screens.Favorites
import com.example.jobstest.screens.LoginFirst
import com.example.jobstest.screens.Messages
import com.example.jobstest.screens.Profile
import com.example.jobstest.screens.Responses
import com.example.jobstest.screens.Search
import com.example.jobstest.viewmodel.JobsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {


    private val jobsViewModel: JobsViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Изначально скрываем меню
        setBottomNavigationEnabled(false)

        // Инициализация загрузки данных
        lifecycleScope.launch {
            jobsViewModel.fetchData()

            if (savedInstanceState == null) {
                // Отобразим первый фрагмент при запуске
                loadFragment(LoginFirst())
            }
            binding.bottomNav.setOnItemSelectedListener { item ->

                when(item.itemId){
                    R.id.searchFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Search()).commit()
                    R.id.favoritesFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Favorites()).commit()
                    R.id.responsesFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Responses()).commit()
                    R.id.messagesFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Messages()).commit()
                    R.id.profileFragment -> supportFragmentManager.beginTransaction().replace(R.id.content, Profile()).commit()
                }
                return@setOnItemSelectedListener true
            }

        }





    }


    // Метод для управления доступностью пунктов меню
    private fun setBottomNavigationEnabled(isEnabled: Boolean) {
        for (i in 0 until binding.bottomNav.menu.size()) {
            binding.bottomNav.menu.getItem(i).isEnabled = isEnabled
        }
    }

    // Метод для загрузки фрагментов
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .commit()
    }

    // Метод для активации меню после перехода на SearchFragment
    fun activateBottomNavigation() {
        setBottomNavigationEnabled(true)
    }
    }
