package com.reemmousa.eduapp.ui.wishlist

import android.os.Bundle
import android.view.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.*
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.databinding.FragmentWishlistBinding
import com.reemmousa.eduapp.ui.adapters.CourseItemOperations
import com.reemmousa.eduapp.ui.adapters.CoursesMainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment(), CourseItemOperations, ActionMode.Callback {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WishlistViewModel by viewModels()
    private val wishlistAdapter: WishlistAdapter by lazy { WishlistAdapter(this) }

    private var multiselection = false
    private var selectedCourses = arrayListOf<WishlistEntity>()
    private var wishlistEntities = listOf<WishlistEntity>()
    private val myViewHolders = arrayListOf<CoursesMainAdapter.MainCoursesVH>()

    private lateinit var wishlistActionMode: ActionMode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        requireActivity().updateStatusBarColor(R.color.grey_E3E2E5)

        viewModel.readWishlistFromDB.observe(viewLifecycleOwner) { wishlist ->
            wishlistEntities = wishlist
            val courses = mutableListOf<Course>()
            if (wishlist.isNotEmpty()) {
                wishlist.forEach { wishlistCourse ->
                    courses.add(wishlistCourse.course)
                }
                wishlistAdapter.setData(courses)
                binding.rvCourses.adapter = wishlistAdapter
            }
            binding.errorLayout.apply {
                if (wishlist.isEmpty()) visible() else invisible()
            }
            binding.btDeleteAll.apply {
                isEnabled = wishlist.isNotEmpty()
            }
            binding.rvCourses.apply {
                if (wishlist.isNotEmpty()) visible() else invisible()
            }
        }

        binding.btDeleteAll.setOnClickListener {
            requireActivity().showAppDialog(
                body = getString(R.string.delete_all_msg),
                posClicked = { deleteAll() },
                negClicked = { }
            )

        }

        return root
    }

    private fun deleteAll() {
        viewModel.deleteAllWishlist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        if (::wishlistActionMode.isInitialized) wishlistActionMode.finish()
        super.onPause()
    }

    override fun onCourseBindViewHolder(holder: CoursesMainAdapter.MainCoursesVH) {
        super.onCourseBindViewHolder(holder)
        myViewHolders.add(holder)
    }

    override fun onCourseClicked(
        position: Int,
        course: Course,
        holder: CoursesMainAdapter.MainCoursesVH
    ) {
        super.onCourseClicked(course)
        if (multiselection) {
            applySelection(wishlistEntities[position], holder)
        }
    }

    override fun onCourseLongClicked(
        position: Int,
        course: Course,
        holder: CoursesMainAdapter.MainCoursesVH
    ) {
        super.onCourseLongClicked(position, course, holder)
        if (!multiselection) {
            multiselection = true
            activity?.startActionMode(this)
            applySelection(wishlistEntities[position], holder)
        } else {
            multiselection = false
        }
    }

    private fun applySelection(
        wishlistEntity: WishlistEntity,
        holder: CoursesMainAdapter.MainCoursesVH
    ) {
        if (selectedCourses.contains(wishlistEntity)) {
            selectedCourses.remove(wishlistEntity)
            changeWishlistCourseStyle(
                holder,
                ContextCompat.getColor(requireContext(), android.R.color.transparent)
            )
            applyActionModeTitle()
        } else {
            selectedCourses.add(wishlistEntity)
            changeWishlistCourseStyle(
                holder,
                ContextCompat.getColor(requireContext(), R.color.action_mode_selected)
            )
            applyActionModeTitle()
        }
    }

    private fun changeWishlistCourseStyle(
        holder: CoursesMainAdapter.MainCoursesVH,
        color: Int
    ) {
        holder.itemView.findViewById<CardView>(R.id.actionModeSelectionCV)
            .setCardBackgroundColor(color)
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.wishlist_contextual_menu, menu)
        wishlistActionMode = actionMode!!
        activity?.updateStatusBarColor(R.color.secondaryDarkColor, false)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_wishlist_menu) {
            selectedCourses.forEach {
                viewModel.deleteCourseFromWishlist(it)
            }
            activity?.showSnackBar(
                "${selectedCourses.size} course(s) removed."
            )
            multiselection = false
            selectedCourses.clear()
            wishlistActionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        multiselection = false
        selectedCourses.clear()
        activity?.updateStatusBarColor(R.color.grey_E3E2E5)
        myViewHolders.forEach { holder ->
            changeWishlistCourseStyle(
                holder,
                ContextCompat.getColor(requireContext(), android.R.color.transparent)
            )
        }
    }

    private fun applyActionModeTitle() {
        when (selectedCourses.size) {
            0 -> wishlistActionMode.finish()
            1 -> wishlistActionMode.title =
                StringBuilder("${selectedCourses.size}").append(" item selected")
            else -> wishlistActionMode.title =
                StringBuilder("${selectedCourses.size}").append(" items selected")
        }
    }
}