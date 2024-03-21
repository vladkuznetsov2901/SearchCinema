package com.example.skillcinema.presentation.welcomeScreens

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import com.example.skillcinema.R
import com.example.skillcinema.api.authApiService
import com.example.skillcinema.data.ktorData.LoginData
import com.example.skillcinema.data.ktorData.RegistrationData
import com.example.skillcinema.databinding.ActivityWelcomeBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import com.example.skillcinema.presentation.welcomeScreens.adapters.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeActivity : AppCompatActivity() {

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val activities = ArrayList<Activity>()

    private lateinit var binding: ActivityWelcomeBinding

    private var titleList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activities.add(this)

        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstTime = sharedPref.getBoolean("isFirstTime", true)

//        if (isFirstTime) {
//            val editor = sharedPref.edit()
//            editor.putBoolean("isFirstTime", false)
//            editor.apply()
//            lifecycleScope.launch {
//                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
//                    profileViewModel.insertCollections()
//                }
//            }
//        } else {
//            val intent = Intent(
//                this,
//                com.example.skillcinema.presentation.homeScreens.HomeActivity::class.java
//            )
//            startActivity(intent)
//        }

        postToList()

        binding.viewPager.adapter = ViewPagerAdapter(imageList, titleList)
        binding.viewPager.isSaveFromParentEnabled = false

        binding.indicator.setViewPager(binding.viewPager)

        binding.indicator.tintIndicator(getColor(R.color.black))


        binding.skipButton.setOnClickListener {

            binding.viewPager.isVisible = false

            binding.skipButton.visibility = View.GONE

            binding.indicator.visibility = View.GONE

            findNavController(R.id.nav_host_fragment).navigate(R.id.action_welcomeFragment_to_loaderFragment)

        }

    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    private fun addToList(title: String, image: Int) {
        titleList.add(title)
        imageList.add(image)
    }

    private fun postToList() {
        addToList("Узнавай о премьерах", R.drawable.welcome_img1)
        addToList("Создавай коллекции", R.drawable.welcome_img2)
        addToList("Узнавай о премьерах", R.drawable.welcome_img3)


    }

    fun testRegistration() {
        val registrationData = RegistrationData("testUser", "test@example.com","testPassword")

        // Запуск в фоновом потоке
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val registerResponse = authApiService.registerUser(registrationData)
                // Обработка успешного ответа, например, вывод в лог
                Log.d("ServerConnection", "Registration successful. Token: ${registerResponse.token}")
            } catch (e: Exception) {
                // Обработка ошибки, например, вывод в лог
                Log.e("ServerConnection", "Registration failed. Error: ${e.message}")
            }
        }
    }

    // Пример выполнения запроса на вход
    fun testLogin() {
        val loginData = LoginData("test2", "test")

        // Запуск в фоновом потоке
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val loginResponse = authApiService.loginUser(loginData)
                // Обработка успешного ответа, например, вывод в лог
                Log.d("ServerConnection", "Login successful. Token: ${loginResponse.token}")
            } catch (e: Exception) {
                // Обработка ошибки, например, вывод в лог
                Log.e("ServerConnection", "Login failed. Error: ${e.message}")
            }
        }
    }

}

