package com.example.skillcinema.presentation.loginScreens.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.api.authApiService
import com.example.skillcinema.data.ktorData.LoginData
import com.example.skillcinema.databinding.FragmentLoginFormBinding
import com.example.skillcinema.presentation.homeScreens.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFormFragment : Fragment() {

    private var _binding: FragmentLoginFormBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    private val viewModel: LoginViewModel by viewModels { loginViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoginFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var login: String = ""
        var password: String = ""

        binding.loginForm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                login = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.passwordForm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                password = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.loginBtn.setOnClickListener {
            val loginData = LoginData(login, password)

            // Запуск в фоновом потоке
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val loginResponse = authApiService.loginUser(loginData)
                    activity?.runOnUiThread {
                        saveTokenToSharedPreferences(loginResponse.token)
                        val intent = Intent(context, HomeActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    activity?.runOnUiThread {
                        Log.e("ServerConnection", "Login failed. Error: ${e.message}")

                        showErrorDialog("Invalid login/password")
                    }
                }
            }
        }



        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun saveTokenToSharedPreferences(token: String) {
        // Пример сохранения токена в SharedPreferences (замените на ваш механизм сохранения)
        val sharedPreferences =
            requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_token", token)
        editor.apply()
    }

    private fun showErrorDialog(message: String) {
        // Пример отображения диалога с сообщением об ошибке
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }


}