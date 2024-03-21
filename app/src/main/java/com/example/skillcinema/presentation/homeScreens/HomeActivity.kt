package com.example.skillcinema.presentation.homeScreens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityHomeBinding
import com.example.skillcinema.presentation.profileScreens.ProfileActivity
import com.example.skillcinema.presentation.searchScreens.SearchActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intentSearch = Intent(this, SearchActivity::class.java)
        val intentProfile = Intent(this, ProfileActivity::class.java)


        binding.bottomAppBar.selectedItemId = R.id.home

        binding.bottomAppBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    if (findNavController(R.id.home_nav_host).currentDestination?.id != R.id.homeFragment) {
                        findNavController(R.id.home_nav_host).navigate(R.id.homeFragment)
                        true
                    } else {
                        false
                    }
                }

                R.id.search -> {
                    startActivity(intentSearch)
                    finish()
                    true
                }

                R.id.profile -> {
                    startActivity(intentProfile)
                    finish()
                    true
                }

                else -> false
            }
        }

    }
}

