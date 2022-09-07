package com.lovrekovic.basketmanager.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.lovrekovic.basketmanager.R
import com.lovrekovic.basketmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.Activity_Main_FragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.ActivityMainBottomNavigation.setOnItemSelectedListener(bottomNavListener)
        binding.ActivityMainBottomNavigation.setupWithNavController(findNavController(R.id.Activity_Main_FragmentContainerView))

    }

    private val bottomNavListener =
        NavigationBarView.OnItemSelectedListener { item ->
            Log.d("MainActivity", "item: ${item.itemId}")
            when (item.itemId) {
                R.id.maps -> {
                    navController.navigate(R.id.maps)
                    true
                }
                R.id.all -> {
                    navController.navigate(R.id.all)
                    true
                }
                R.id.new_game -> {
                    navController.navigate(R.id.new_game)
                    true
                }
                R.id.profile -> {
                    navController.navigate(R.id.profile)
                    true
                }
                else -> false
            }
        }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        return navController.navigateUp()
    }
}