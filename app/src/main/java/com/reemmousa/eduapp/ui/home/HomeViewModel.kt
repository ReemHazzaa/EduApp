package com.reemmousa.eduapp.ui.home

import android.app.Application
import com.reemmousa.eduapp.dataStructures.HomeCategory
import com.reemmousa.eduapp.repository.Repository
import com.reemmousa.eduapp.ui.allCourses.AllCoursesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: Repository,
    application: Application
) : AllCoursesViewModel(repository, application) {

    val homeCategories: List<HomeCategory> = repository.constants.homeCategories

}