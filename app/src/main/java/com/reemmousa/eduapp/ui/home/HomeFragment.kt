package com.reemmousa.eduapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.*
import com.reemmousa.eduapp.databinding.FragmentHomeBinding
import com.reemmousa.eduapp.ui.home.adapters.HomeCategoriesAdapter
import com.reemmousa.eduapp.ui.home.adapters.HomeCoursesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val TAG = "HomeFragment"
    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentHomeBinding? = null
    private lateinit var coursesAdapter: HomeCoursesAdapter

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        coursesAdapter = HomeCoursesAdapter(findNavController())

        reactOnNetworkState()
        setUpUI()
        loadDataFromCacheWhenOnline()

        return root
    }

    private fun requestApiData() {
        Log.i(TAG, "requestApiData called")
        homeViewModel.getAllCourses()
        homeViewModel.allCoursesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showLoadingUI()
                }
                is NetworkResult.Error -> {
                    stopLoadingUI()
                    loadDataFromCacheWhenOffline()
                    requireActivity().showSnackBar(
                        response.message.toString(),
                        getString(R.string.ok)
                    ) {}
                }
                is NetworkResult.Success -> {
                    stopLoadingUI()
                    listOfNotNull(response.data!!.courses).let {
                        coursesAdapter.setData(response.data.courses)
                    }
                    response.data.courses?.all { (it != null) }.apply {
                        coursesAdapter.setData(response.data.courses)
                    }
                }
            }
        }
    }

    private fun loadDataFromCacheWhenOnline() {
        lifecycleScope.launch {
            homeViewModel.coursesInDatabase.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.i(TAG, "loadDataFromCacheWhenOnline called")
                    stopLoadingUI()
                    coursesAdapter.setData(database[0].responseCoursesList.courses)
                } else {
                    requestApiData()
                }
            }
        }
    }

    private fun loadDataFromCacheWhenOffline() {
        lifecycleScope.launch {
            homeViewModel.coursesInDatabase.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.i(TAG, "loadDataFromCacheWhenOffline called")
                    stopLoadingUI()
                    coursesAdapter.setData(database[0].responseCoursesList.courses)
                }
            }
        }
    }

    private fun showLoadingUI() {
        binding.apply {
            swipeRefresh.isRefreshing = true
        }
    }

    private fun stopLoadingUI() {
        binding.swipeRefresh.apply {
            isRefreshing = false
            isEnabled = true
        }
    }

    private fun setUpUI() {
        requireActivity().updateStatusBarColor(R.color.primaryColor, false)
        binding.rvCourses.adapter = coursesAdapter
        binding.rvCategories.apply {
            adapter = HomeCategoriesAdapter(
                homeViewModel.homeCategories,
                findNavController()
            )
        }

        binding.swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = true
                loadDataFromCacheWhenOnline()
            }
        }
        setClickListeners()
    }

    private fun setClickListeners() {
        binding.searchView.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_searchFragment)
            }
        }

        binding.tvViewAllCourses.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_allCoursesFragment)
            }
        }
    }

    private fun reactOnNetworkState() {
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.isConnected(requireContext()).collect { status ->
                Log.e(TAG, status.toString())
                if (!status) {
                    requireActivity().showSnackBar(
                        message = getString(R.string.not_connected)

                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}