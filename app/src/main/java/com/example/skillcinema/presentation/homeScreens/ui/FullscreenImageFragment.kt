package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillcinema.adapters.FullscreenImagesAdapter
import com.example.skillcinema.databinding.FragmentFullscreenImageBinding


class FullscreenImageFragment : Fragment() {

    private var _binding: FragmentFullscreenImageBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFullscreenImageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPager = binding.viewPager
        val imagesAdapter = FullscreenImagesAdapter()
        viewPager.adapter = imagesAdapter

        val imageUrls = arguments?.getStringArrayList("imageUrls")
        val startPosition = arguments?.getInt("startPosition", 0) ?: 0

        if (!imageUrls.isNullOrEmpty()) {
            imagesAdapter.submitList(imageUrls)
            viewPager.setCurrentItem(startPosition, false)
        }
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() {}

    }
}