package com.reemmousa.eduapp.ui.filter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.reemmousa.eduapp.app.Constants
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo
import com.reemmousa.eduapp.dataStructures.filter.FilterPrefs
import com.reemmousa.eduapp.databinding.FragmentFilterBinding
import com.reemmousa.eduapp.ui.allCourses.AllCoursesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllCoursesViewModel by viewModels()

    private var price: String = Constants.COURSE_PRICE_FREE
    private var category: String = Constants.DEFAULT_COURSE_CATEGORY
    private var priceChipId: Int = 0
    private var categoryChipId: Int = 0

    private val dataStoreRepo: DataStoreRepo by lazy {
        DataStoreRepo(this.requireContext().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        readChipSelectedFromDataStore()

        setCheckChangeListenersForChipGroups()

        binding.btApply.setOnClickListener {
            saveChipSelectionInDataStore()
            val action = FilterFragmentDirections.actionFilterFragmentToAllCoursesFragment(true)
            findNavController().navigate(action)
        }

        binding.btReset.setOnClickListener {
            resetChipSelectionInDataStore()
            val action = FilterFragmentDirections.actionFilterFragmentToAllCoursesFragment(false)
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun readChipSelectedFromDataStore() {
        viewModel.readPrefs.asLiveData().observe(viewLifecycleOwner) { value ->
            price = value.selectedCoursePrice
            category = value.selectedCourseCategory

            updateUIChips(value.selectedCoursePriceId, binding.cgPrice)
            updateUIChips(value.selectedCourseCategoryId, binding.cgCategory)
        }
    }

    private fun updateUIChips(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.check(chipId)
//                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.e("FilterBottomSheet", e.message.toString())
            }
        }
    }

    private fun setCheckChangeListenersForChipGroups() {
        binding.cgPrice.children
            .toList()
            .forEach { view ->
                (view as Chip).setOnCheckedChangeListener { compoundButton, checked ->
                    if (checked) {
                        val selectedPrice = compoundButton.text.toString()
                        val selectedPriceId = compoundButton.id
                        price = selectedPrice
                        priceChipId = selectedPriceId
                    }
                }
            }

        binding.cgCategory.children
            .toList()
            .forEach { view ->
                (view as Chip).setOnCheckedChangeListener { compoundButton, checked ->
                    if (checked) {
                        val selectedCategory = compoundButton.text.toString()
                        val selectedCategoryId = compoundButton.id
                        category = selectedCategory
                        categoryChipId = selectedCategoryId
                    }
                }
            }
    }

    private fun saveChipSelectionInDataStore() {
        viewModel.saveAppPrefs(
            FilterPrefs(
                selectedCoursePrice = price,
                selectedCourseCategory = category,
                selectedCoursePriceId = priceChipId,
                selectedCourseCategoryId = categoryChipId
            )
        )
    }

    private fun resetChipSelectionInDataStore() {
        viewModel.saveAppPrefs(
            FilterPrefs(
                selectedCoursePrice = Constants.COURSE_PRICE_FREE,
                selectedCourseCategory = Constants.DEFAULT_COURSE_CATEGORY,
                selectedCoursePriceId = 0,
                selectedCourseCategoryId = 0
            )
        )
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}