package com.example.skillcinema.presentation.searchScreens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivitySearchBinding
import com.example.skillcinema.presentation.profileScreens.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomAppBar.selectedItemId = R.id.search


        val intentHome = Intent(
            applicationContext,
            com.example.skillcinema.presentation.homeScreens.HomeActivity::class.java
        )
        val intentProfile = Intent(
            applicationContext,
            ProfileActivity::class.java
        )

        binding.bottomAppBar.selectedItemId = R.id.search

        binding.bottomAppBar.setOnItemSelectedListener {

            if (it.itemId == R.id.home) {
                startActivity(intentHome)
                finish()
            }

            if (it.itemId == R.id.profile) {
                startActivity(intentProfile)
                finish()
            }

            if (it.itemId == R.id.search) return@setOnItemSelectedListener true

            true

        }


    }
}