package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.databinding.FragmentCollectionNameDialogBinding
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModel
import com.example.skillcinema.presentation.profileScreens.ui.main.ProfileViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CollectionNameDialog : DialogFragment() {

    private var _binding: FragmentCollectionNameDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profileViewModelFactory: ProfileViewModelFactory

    private val viewModel: ProfileViewModel by viewModels { profileViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCollectionNameDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filmID = arguments?.getInt("tempID")
        Log.d("IDDDDDDDDDDDDDCOLLECTION", filmID.toString())

        var nameCollection = ""

        binding.editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                nameCollection = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.saveBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.checkNamesCollections(nameCollection).onEach { exists ->
                    if (exists) {
                        dismiss()
                        showErrorDialog()
                    } else {
                        viewModel.createNewCollection(nameCollection)
                        val bundle = Bundle()
                        filmID?.let { it1 -> bundle.putInt("itemID", it1) }
                        showDialog(bundle)
                        dismiss()
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }

        binding.closeBtn.setOnClickListener {
            dismiss()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showDialog(bundle: Bundle) {
        val bottomSheetDialog = BottomSheetDialog()
        bottomSheetDialog.arguments = bundle
        bottomSheetDialog.show(parentFragmentManager, bottomSheetDialog.tag)
    }

    private fun showErrorDialog() {
        val errorDialog = ErrorFragment()
        errorDialog.show(parentFragmentManager, errorDialog.tag)
    }

}