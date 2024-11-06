package com.example.aplikasidicodingevent.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.aplikasidicodingevent.R
import com.example.aplikasidicodingevent.databinding.ActivityHomeBinding
import com.example.aplikasidicodingevent.ui.setting.SettingPreferences
import com.example.aplikasidicodingevent.ui.setting.SettingViewModel
import com.example.aplikasidicodingevent.ui.setting.SettingViewModelFactory

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize theme
        val pref = SettingPreferences.getInstance(this)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_home) as NavHostFragment
        navController = navHostFragment.navController

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.navView.visibility = when (destination.id) {
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_setting -> View.VISIBLE
                else -> View.GONE
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            val currentFragment = navController.currentDestination?.id
            when (currentFragment) {
                R.id.navigation_detail -> {
                    navController.navigateUp()
                    binding.navView.visibility = View.VISIBLE
                }
                else -> finish()
            }
        }
    }
}