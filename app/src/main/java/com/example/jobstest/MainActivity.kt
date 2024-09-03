package com.example.jobstest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val jobsViewModel: JobsViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private var isMenuEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState != null) {
            isMenuEnabled = savedInstanceState.getBoolean("MENU_ENABLED", false)
            setBottomNavigationEnabled(isMenuEnabled)
        } else {
            setBottomNavigationEnabled(false)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.searchFragment -> loadFragment(Search())
                R.id.favoritesFragment -> loadFragment(Favorites())
                R.id.responsesFragment -> loadFragment(Responses())
                R.id.messagesFragment -> loadFragment(Messages())
                R.id.profileFragment -> loadFragment(Profile())
            }
            true
        }

        lifecycleScope.launch(Dispatchers.IO) {
            jobsViewModel.fetchData()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            jobsViewModel.favoriteVacancies.collect { favorites ->
                updateFavoritesBadge(favorites.size)
            }
        }

        if (savedInstanceState == null) {
            loadFragment(LoginFirst())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("MENU_ENABLED", isMenuEnabled)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isMenuEnabled = savedInstanceState.getBoolean("MENU_ENABLED", false)
        setBottomNavigationEnabled(isMenuEnabled)
    }

    private fun updateFavoritesBadge(count: Int) {
        val badge = binding.bottomNav.getOrCreateBadge(R.id.favoritesFragment)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
            badge.backgroundColor = resources.getColor(R.color.red, null) // Устанавливаем красный фон
        } else {
            badge.isVisible = false
        }
    }

    private fun setBottomNavigationEnabled(isEnabled: Boolean) {
        for (i in 0 until binding.bottomNav.menu.size()) {
            binding.bottomNav.menu.getItem(i).isEnabled = isEnabled
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.content, fragment)
            .commit()
    }

    fun activateBottomNavigation() {
        isMenuEnabled = true
        setBottomNavigationEnabled(true)
    }
}
