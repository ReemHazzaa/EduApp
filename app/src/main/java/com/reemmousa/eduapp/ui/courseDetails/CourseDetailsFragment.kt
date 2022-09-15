package com.reemmousa.eduapp.ui.courseDetails

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.*
import com.reemmousa.eduapp.dataSources.local.room.entities.CartEntity
import com.reemmousa.eduapp.dataSources.local.room.entities.WishlistEntity
import com.reemmousa.eduapp.dataStructures.courses.Course
import com.reemmousa.eduapp.dataStructures.reviews.ResponseCourseReview
import com.reemmousa.eduapp.databinding.FragmentCourseDetailsBinding
import com.reemmousa.eduapp.ui.cart.CartViewModel
import com.reemmousa.eduapp.ui.courseDetails.adapters.InstructorsAdapter
import com.reemmousa.eduapp.ui.courseDetails.adapters.LecturesAdapter
import com.reemmousa.eduapp.ui.courseDetails.adapters.ReviewsAdapter
import com.reemmousa.eduapp.ui.wishlist.WishlistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CourseDetailsFragment : Fragment() {
    private val args: CourseDetailsFragmentArgs by navArgs()
    private lateinit var selectedCourse: Course

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CourseDetailsViewModel by viewModels()
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val instructorsAdapter: InstructorsAdapter by lazy { InstructorsAdapter() }
    private val lecturesAdapter: LecturesAdapter by lazy { LecturesAdapter() }
    private val reviewsAdapter: ReviewsAdapter by lazy { ReviewsAdapter() }

    private var courseWishlistEd = false
    private var courseWishlistEdId = 0

    private var courseInCart = false
    private var courseInCartId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectedCourse = args.selectedCourse

        setCourseDetailsUI(selectedCourse)
        loadCourseLectures(selectedCourse.courseId.toString(), 1)
        loadCourseReviews(selectedCourse.courseId.toString(), 1)

        setUpRefreshLogic()

        checkWishlistEdItem(binding.btAddToWishlist)
        binding.btAddToWishlist.setOnClickListener {
            if (courseWishlistEd) {
                removeFromWishlist()
            } else {
                saveToWishlist()
            }
        }

        checkInCartItem(binding.btAddToCart)
        binding.btAddToCart.setOnClickListener {
            if (courseInCart) {
                removeFromCart()
            } else {
                saveToCart()
            }
        }

        return root
    }

    private fun checkWishlistEdItem(button: MaterialButton) {
        wishlistViewModel.readWishlistFromDB.observe(viewLifecycleOwner) { wishlistEntity ->
            try {
                val co = wishlistEntity.find { it.course.courseId == selectedCourse.courseId }
                if (co != null) {   // course is in wishlist
                    changeWishListButtonState(
                        button,
                        getString(R.string.wishlist_ed),
                        activity?.getDrawable(R.drawable.ic_favorite_filled)!!
                    )
                    courseWishlistEdId = co.id
                    courseWishlistEd = true
                } else {
                    changeWishListButtonState(
                        button,
                        getString(R.string.title_Wishlist),
                        activity?.getDrawable(R.drawable.ic_favorite)!!
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString(), e)
            }
        }
    }

    private fun checkInCartItem(button: MaterialButton) {
        cartViewModel.readCartFromDB.observe(viewLifecycleOwner) { cartEntity ->
            try {
                val co = cartEntity.find { it.course.courseId == selectedCourse.courseId }
                if (co != null) {   // course is in cart
                    changeAddToCartButtonState(
                        button,
                        getString(R.string.in_cart),
                    )
                    courseInCartId = co.id
                    courseInCart = true
                } else {
                    changeAddToCartButtonState(
                        button,
                        getString(R.string.add_to_cart),
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString(), e)
            }
        }
    }

    private fun saveToWishlist() {
        val entity = WishlistEntity(0, selectedCourse)
        viewModel.addToWishlist(entity)
        changeWishListButtonState(
            binding.btAddToWishlist,
            getString(R.string.wishlist_ed),
            activity?.getDrawable(R.drawable.ic_favorite_filled)!!
        )
        activity?.showSnackBar(getString(R.string.course_wishlisted))
        courseWishlistEd = true
    }

    private fun saveToCart() {
        val entity = CartEntity(0, selectedCourse)
        viewModel.addToCart(entity)
        changeAddToCartButtonState(
            binding.btAddToCart,
            getString(R.string.in_cart)
        )
        activity?.showSnackBar(getString(R.string.course_added_to_cart))
        courseInCart = true
    }

    private fun removeFromWishlist() {
        val entity = WishlistEntity(courseWishlistEdId, selectedCourse)
        wishlistViewModel.deleteCourseFromWishlist(entity)
        changeWishListButtonState(
            binding.btAddToWishlist,
            getString(R.string.title_Wishlist),
            activity?.getDrawable(R.drawable.ic_favorite)!!
        )
        activity?.showSnackBar(getString(R.string.removed_from_wishlist))
        courseWishlistEd = false
    }

    private fun removeFromCart() {
        val entity = CartEntity(courseInCartId, selectedCourse)
        cartViewModel.deleteCourseFromCart(entity)
        changeAddToCartButtonState(
            binding.btAddToWishlist,
            getString(R.string.add_to_cart),
        )
        activity?.showSnackBar(getString(R.string.removed_from_cart))
        courseInCart = false
    }

    private fun changeWishListButtonState(
        button: MaterialButton,
        buttonTxt: String,
        drawable: Drawable
    ) {
        button.text = buttonTxt
        button.icon = drawable
    }

    private fun changeAddToCartButtonState(
        button: MaterialButton,
        buttonTxt: String,
    ) {
        button.text = buttonTxt
    }

    private fun loadCourseDetails(courseId: String) {
        viewModel.getCourse(courseId)
        viewModel.course.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showLoading()
                    hideErrorUI()
                }
                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorUI()
                    requireActivity().showSnackBar(
                        response.message.toString(),
                        getString(R.string.ok)
                    ) {}
                }
                is NetworkResult.Success -> {
                    hideLoading()
                    hideLoading()
                    if (response.data != null) {
                        setCourseDetailsUI(response.data)
                    }
                }
            }
        }
    }

    private fun loadCourseReviews(courseId: String, pageNo: Int) {
        viewModel.getReviews(courseId, pageNo)
        viewModel.reviewsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    showLoading()
                    hideErrorUI()
                }
                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorUI()
                    requireActivity().showSnackBar(
                        response.message.toString(),
                        getString(R.string.ok)
                    ) {}

                }
                is NetworkResult.Success -> {
                    hideLoading()
                    hideErrorUI()
                    if (response.data?.reviews?.isNotEmpty() == true) {
                        displayReviews(response)

                    }
                    response.data?.reviews?.all { (it != null) }.apply {
                        binding.apply {
                            if (response.data?.reviews?.size == 0) {
                                tvReviews.invisible()
                                rvReviews.invisible()

                            } else {
                                displayReviews(response)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun displayReviews(response: NetworkResult.Success<ResponseCourseReview>) {
        binding.tvReviews.visible()
        binding.rvReviews.visible()
        reviewsAdapter.setData(response.data!!.reviews)
        binding.rvReviews.adapter = reviewsAdapter
    }

    private fun setCourseDetailsUI(course: Course) {
        course.apply {
            binding.apply {
                image480x270?.let { ivCourseImage.loadImageWithPicasso(it) }
                tvCourseName.text = title.toString()
                tvCourseHeadline.text = headline.toString()

                instructorsAdapter.setData(visibleInstructors)
                rvInstructors.adapter = instructorsAdapter
                tvCoursePrice.text = price
            }
        }
    }

    private fun loadCourseLectures(courseId: String, pageNo: Int) {
        viewModel.getCourseLectures(courseId, pageNo)
        viewModel.courseLecturesResponse.observe(viewLifecycleOwner) { it ->
            when (it) {
                is NetworkResult.Loading -> {
                    showLoading()
                    hideErrorUI()
                }
                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorUI()
                    requireActivity().showSnackBar(
                        it.message.toString(),
                        getString(R.string.ok)
                    ) {}

                }
                is NetworkResult.Success -> {
                    hideLoading()
                    hideErrorUI()
                    it.data?.lectures?.all { (it != null) }.apply {
                        lecturesAdapter.setData(it.data?.lectures)
                        binding.rvLectures.adapter = lecturesAdapter
                    }
                }
            }
        }
    }

    private fun setUpRefreshLogic() {
        hideErrorUI()
        binding.apply {
            swipeRefresh.apply {
                isRefreshing = true
                setOnRefreshListener {
                    rvLectures.adapter = null
                    rvReviews.adapter = null
                    rvInstructors.adapter = null
                    loadCourseDetails(selectedCourse.courseId.toString())
                    loadCourseLectures(selectedCourse.courseId.toString(), 1)
                    loadCourseReviews(selectedCourse.courseId.toString(), 1)
                }
            }
        }
    }

    private fun hideErrorUI() {
        binding.apply {
            errorCurr.root.invisible()
            errorReviews.root.invisible()
        }
    }

    private fun showErrorUI() {
        binding.apply {
            errorCurr.root.visible()
            errorCurr.root.findViewById<TextView>(R.id.tvError).text =
                getString(R.string.error_curr)
            errorReviews.root.visible()
            errorReviews.root.findViewById<TextView>(R.id.tvError).text =
                getString(R.string.error_reviews)
        }
    }

    private fun showLoading() {
        binding.apply {
            swipeRefresh.isRefreshing = true
        }
    }

    private fun hideLoading() {
        binding.swipeRefresh.apply {
            isRefreshing = false
            isEnabled = true
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}