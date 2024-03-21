package com.example.skillcinema.presentation.homeScreens.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.skillcinema.R
import com.example.skillcinema.adapters.ImagesAdapter
import com.example.skillcinema.data.Image
import com.example.skillcinema.data.ItemType
import com.example.skillcinema.databinding.FragmentImagesListPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImagesListPage : Fragment() {

    private var _binding: FragmentImagesListPageBinding? = null
    private val binding get() = _binding!!


    @Inject
    lateinit var homeViewModelFactory: HomeViewModelFactory

    private val viewModel: HomeViewModel by viewModels { homeViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentImagesListPageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filmID = arguments?.getInt("filmId")

        val imagesAdapter = ImagesAdapter()
        binding.recyclerViewFull.adapter = imagesAdapter
        val layoutManager = GridLayoutManager(context, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (imagesAdapter.getItemViewType(position) == ItemType.FULL_WIDTH_IMAGE.ordinal) {
                    2
                } else {
                    1
                }
            }
        }

        binding.recyclerViewFull.layoutManager =
            layoutManager

        binding.chipFrames.isChecked = true


        var stillImg = listOf<Image>()
        var shootingImg = listOf<Image>()
        var postersImg = listOf<Image>()


        lifecycleScope.launch {
            binding.chipFrames.isChecked = true
            binding.chipFilming.isChecked = false
            binding.chipPosters.isChecked = false
            stillImg = viewModel.loadMovieImg(filmID!!, "STILL")
            shootingImg = viewModel.loadMovieImg(filmID, "SHOOTING")
            postersImg = viewModel.loadMovieImg(filmID, "POSTER")

            val stillImgText = "${binding.chipFrames.text} ${stillImg.size}"
            val shootingImgText = "${binding.chipFilming.text} ${shootingImg.size}"
            val postersImgText = "${binding.chipPosters.text} ${postersImg.size}"

            binding.chipFrames.text = stillImgText
            binding.chipFilming.text = shootingImgText
            binding.chipPosters.text = postersImgText

            imagesAdapter.submitList(stillImg)
        }

        val images: MutableList<
                List<String>> = emptyList<List<String>>().toMutableList()

        images.add(stillImg.map { it.imageUrl })



        binding.chipFrames.setOnClickListener {
            binding.chipFrames.isChecked = true
            binding.chipFilming.isChecked = false
            binding.chipPosters.isChecked = false
            imagesAdapter.submitList(stillImg)
        }

        binding.chipFilming.setOnClickListener {
            binding.chipFrames.isChecked = false
            binding.chipFilming.isChecked = true
            binding.chipPosters.isChecked = false
            imagesAdapter.submitList(shootingImg)

        }


        binding.chipPosters.setOnClickListener {
            binding.chipFrames.isChecked = false
            binding.chipFilming.isChecked = false
            binding.chipPosters.isChecked = true
            imagesAdapter.submitList(postersImg)

        }

        imagesAdapter.setOnItemClickListener(object : ImagesAdapter.OnItemClickListener {
            override fun onItemClick(imgURL: String) {
                // Определите текущий тип изображения
                val currentImageType = when {
                    binding.chipFrames.isChecked -> "STILL"
                    binding.chipFilming.isChecked -> "SHOOTING"
                    binding.chipPosters.isChecked -> "POSTER"
                    else -> ""
                }

                // Получите соответствующий список изображений
                val currentImages = when (currentImageType) {
                    "STILL" -> stillImg
                    "SHOOTING" -> shootingImg
                    "POSTER" -> postersImg
                    else -> emptyList()
                }

                // Передайте списки изображений в FullscreenImageFragment
                val bundle = Bundle().apply {
                    putStringArrayList("imageUrls", ArrayList(currentImages.map { it.imageUrl }))
                    putInt("startPosition", currentImages.indexOfFirst { it.imageUrl == imgURL })
                }

                findNavController().navigate(
                    R.id.action_imagesListPage_to_fullscreenImageFragment,
                    bundle
                )
            }
        })

        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}