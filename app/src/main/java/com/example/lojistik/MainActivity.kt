package com.example.lojistik

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.example.lojistik.model.UserData
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    /**
     * Holds the currently logged-in user's data.
     * Accessible by fragments to display user-specific information.
     */
    var currentUser: UserData? = null
        private set

    /**
     * Shared list of notifications.
     */
    val notificationsList = mutableListOf<Map<String, String>>()

    fun addNotification(title: String, body: String) {
        val notification = mapOf(
            "title" to title,
            "body" to body,
            "time" to "Az önce"
        )
        notificationsList.add(0, notification) // Add to top
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Start with login fragment and hide bottom nav
        if (savedInstanceState == null) {
            logout()
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
     * Logs out the user, clears user data, and returns to the login screen.
     * Called from the profile screen.
     */
    fun logout() {
        currentUser = null
        bottomNavigation.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, login())
            .commit()
    }

    /**
     * Called from LoginFragment after successful authentication.
     * Stores user data and navigates to the main dashboard.
     *
     * @param userData The authenticated user's profile data
     */
    fun onLoginSuccess(userData: UserData?) {
        currentUser = userData
        navigateToMain()
    }

    /**
     * Called from LoginFragment after successful login.
     * Shows the bottom navigation and loads the dashboard.
     * For ADMINs, hides bottom nav and shows Admin Dashboard.
     */
    fun navigateToMain() {
        if (currentUser?.role == "ADMIN") {
            bottomNavigation.visibility = View.GONE
            loadFragment(AdminDashboardFragment())
        } else {
            bottomNavigation.visibility = View.VISIBLE
            bottomNavigation.selectedItemId = R.id.nav_dashboard
            loadFragment(dashboard())
        }
    }

    /**
     * Navigates to the registration screen.
     * Called from LoginFragment when user taps "Don't have an account? Register".
     */
    fun navigateToRegister() {
        bottomNavigation.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragmentContainer, register.newInstance())
            .commit()
    }

    /**
     * Navigates back to the login screen.
     * Called from RegisterFragment when user taps "Already have an account? Login".
     */
    fun navigateToLogin() {
        bottomNavigation.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragmentContainer, login())
            .commit()
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
     * Handle back press: navigate appropriately based on current screen.
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        when (currentFragment) {
            is login -> {
                // If on login screen, exit app
                super.onBackPressed()
            }
            is register -> {
                // If on register screen, go back to login
                navigateToLogin()
            }
            else -> {
                // If on any main screen, go back to login
                logout()
            }
        }
    }
}
