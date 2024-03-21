package com.example.skillcinema.presentation.loginScreens.ui.main

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
import com.example.skillcinema.data.ktorData.RegistrationData
import com.example.skillcinema.databinding.FragmentRegisterFormBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegisterFormFragment : Fragment() {

    private var _binding: FragmentRegisterFormBinding? = null
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
        _binding = FragmentRegisterFormBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var login: String = ""
        var email: String = ""
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

        binding.emailForm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                email = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.registerBtn.setOnClickListener {
            val registrationData = RegistrationData(login, email, password)

            // Запуск в фоновом потоке
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val registerResponse = authApiService.registerUser(registrationData)
                    activity?.runOnUiThread {
                        showAccessDialog("You have successfully registered! You can log in!")
                        findNavController().popBackStack()
                    }
                } catch (e: Exception) {
                    activity?.runOnUiThread {
                        Log.e("ServerConnection", "Login failed. Error: ${e.message}")

                        showErrorDialog("Invalid login/password/email")
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

    private fun showErrorDialog(message: String) {
        // Пример отображения диалога с сообщением об ошибке
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showAccessDialog(message: String) {
        // Пример отображения диалога с сообщением об ошибке
        AlertDialog.Builder(requireContext())
            .setTitle("Access")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

}