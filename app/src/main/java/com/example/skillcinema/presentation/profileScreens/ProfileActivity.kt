package com.example.skillcinema.presentation.profileScreens

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.skillcinema.R
import com.example.skillcinema.databinding.ActivityProfileBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import com.example.skillcinema.presentation.searchScreens.SearchActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomAppBar.selectedItemId = R.id.profile

        val intentHome = Intent(
            applicationContext,
            com.example.skillcinema.presentation.homeScreens.HomeActivity::class.java
        )
        val intentSearch = Intent(
            applicationContext,
            SearchActivity::class.java
        )

        binding.bottomAppBar.selectedItemId = R.id.profile

        binding.bottomAppBar.setOnItemSelectedListener {

            if (it.itemId == R.id.home) {
                startActivity(intentHome)
                finish()
            }

            if (it.itemId == R.id.search) {
                startActivity(intentSearch)
                finish()
            }

            if (it.itemId == R.id.profile) return@setOnItemSelectedListener true

            true

        }



    }
}