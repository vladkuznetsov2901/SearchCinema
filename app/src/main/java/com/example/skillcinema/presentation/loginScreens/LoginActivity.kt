package com.example.skillcinema.presentation.loginScreens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.skillcinema.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }
    override fun onPause() {
        super.onPause()
        finish()
    }

}