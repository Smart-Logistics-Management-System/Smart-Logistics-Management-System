package com.example.lojistik

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Start with login fragment and hide bottom nav
        if (savedInstanceState == null) {
            showLoginScreen()
        }

        // Setup bottom navigation item selection
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadFragment(dashboard())
                    true
                }
                R.id.nav_packages -> {
                    loadFragment(list())
                    true
                }
                R.id.nav_notifications -> {
                    loadFragment(notifications())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(profile())
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Shows the login screen and hides the bottom navigation bar.
     */
    private fun showLoginScreen() {
        bottomNavigation.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, login())
            .commit()
    }

    /**
     * Called from LoginFragment after successful login.
     * Shows the bottom navigation and loads the dashboard.
     */
    fun navigateToMain() {
        bottomNavigation.visibility = View.VISIBLE
        bottomNavigation.selectedItemId = R.id.nav_dashboard
        loadFragment(dashboard())
    }

    /**
     * Loads a fragment into the container.
     */
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    /**
     * Handle back press: if on main screens, go back to login.
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is login) {
            // If on login screen, exit app
            super.onBackPressed()
        } else {
            // If on any main screen, go back to login
            showLoginScreen()
        }
    }
}
