package com.reemmousa.eduapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.*
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.databinding.FragmentSearchBinding
import com.reemmousa.eduapp.ui.adapters.CourseItemOperations
import com.reemmousa.eduapp.ui.search.adapters.ResultSearchAdapter
import com.reemmousa.eduapp.ui.search.adapters.TopSearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), CourseItemOperations {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val args: SearchFragmentArgs by navArgs()

    private lateinit var searchesAdapter: TopSearchAdapter
    private val searchViewModel: SearchViewModel by viewModels()
    private val resultSearchAdapter: ResultSearchAdapter by lazy {
        ResultSearchAdapter(this)
    }
    private val courses: MutableLiveData<List<Course?>> = MutableLiveData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().updateStatusBarColor(R.color.grey_E3E2E5, true)

        setTopSearches()

        // Search value passed from HomeCategories
        searchViewModel.selectedTopSearchString.value = args.searchWord

        searchViewModel.selectedTopSearchString.observe(viewLifecycleOwner) { searchWord ->
            searchViewModel.searchCourses(searchWord)
            binding.searchView.setQuery(searchWord, true)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { searchViewModel.searchCourses(it) }
                return false
            }

        })

        searchViewModel.searchResultCourses.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    courses.value = emptyList()
                    binding.apply {
                        swipeRefresh.isRefreshing = true
                    }
                }
                is NetworkResult.Error -> {
                    courses.value = emptyList()
                    binding.swipeRefresh.apply {
                        isRefreshing = false
                        isEnabled = true
                        requireActivity().showSnackBar(
                            response.message.toString(),
                            getString(R.string.ok)
                        ) {}
                    }
                }
                is NetworkResult.Success -> {
                    binding.swipeRefresh.apply {
                        isRefreshing = false
                        isEnabled = true
                    }
                    listOfNotNull(response.data!!.courses).let {
                        resultSearchAdapter.setData(response.data.courses)
                        binding.rvResult.adapter = resultSearchAdapter
                        courses.value = response.data.courses
                    }
                    response.data.courses?.all { (it != null) }.apply {
                        resultSearchAdapter.setData(response.data.courses)
                        binding.rvResult.adapter = resultSearchAdapter
                        courses.value = response.data.courses
                    }
                }
            }
        }

        binding.swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = true
                searchViewModel.searchCourses(searchViewModel.selectedTopSearchString.value ?: "")
            }
        }

        courses.observe(viewLifecycleOwner) {
            binding.errorLayout.apply {
                if (it.isEmpty()) visible() else invisible()
            }

            binding.rvResult.apply {
                if (it.isNotEmpty()) visible() else invisible()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTopSearches() {
        searchesAdapter = TopSearchAdapter(searchViewModel.topSearchesList, searchViewModel)
        binding.rvTopSearch.adapter = searchesAdapter
    }

    override fun onCourseClicked(course: Course) {
        super.onCourseClicked(course)
        val action =
            SearchFragmentDirections.actionSearchFragmentToCourseDetailsFragment(course)
        try {
            findNavController().navigate(action)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}