package com.reemmousa.eduapp.ui.allCourses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.NetworkResult
import com.reemmousa.eduapp.app.showSnackBar
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.databinding.FragmentAllCoursesBinding
import com.reemmousa.eduapp.ui.adapters.CourseItemOperations
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllCoursesFragment : Fragment(), CourseItemOperations {
    private val args by navArgs<AllCoursesFragmentArgs>()
    private var _binding: FragmentAllCoursesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllCoursesViewModel by viewModels()
    private val coursesAdapter: CoursesAdapter by lazy {
        CoursesAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllCoursesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabFilterCourses.setOnClickListener {
            findNavController().navigate(R.id.action_allCoursesFragment_to_filterFragment)
        }

        getCourses()

        binding.swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = true
                getCourses()
            }
        }

        viewModel.allCoursesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.apply {
                        swipeRefresh.isRefreshing = true
                    }
                }
                is NetworkResult.Error -> {
                    binding.swipeRefresh.apply {
                        isRefreshing = false
                        isEnabled = true
                    }
                    requireActivity().showSnackBar(
                        response.message.toString(),
                        getString(R.string.ok)
                    ) {}
                }
                is NetworkResult.Success -> {
                    binding.swipeRefresh.apply {
                        isRefreshing = false
                        isEnabled = true
                    }
                    listOfNotNull(response.data!!.courses).let {
                        coursesAdapter.setData(response.data.courses)
                        binding.rvCourses.adapter = coursesAdapter
                    }
                    response.data.courses?.all { (it != null) }.apply {
                        coursesAdapter.setData(response.data.courses)
                        binding.rvCourses.adapter = coursesAdapter
                    }
                }
            }
        }

        return root
    }

    private fun getCourses() {
        if (args.backFromFilter) {
            viewModel.apply {
                getFilteredCourses(filterParamsFromDataStore())
            }
        } else {
            viewModel.getAllCourses()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCourseClicked(course: Course) {
        super.onCourseClicked(course)
        val action =
            AllCoursesFragmentDirections.actionAllCoursesFragmentToCourseDetailsFragment(course)
        try {
            findNavController().navigate(action)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

}