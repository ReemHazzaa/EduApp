package com.reemmousa.eduapp.dataSources.constants

import android.app.Application
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.Constants
import com.reemmousa.eduapp.app.loadDrawable
import com.reemmousa.eduapp.dataStructures.HomeCategory
import javax.inject.Inject

class ConstantsDataSource @Inject constructor(
    application: Application
) {
    val homeCategories: List<HomeCategory> = listOf(
        HomeCategory(Constants.SEARCH_DEVELOPMENT, application.loadDrawable(R.drawable.coding)),
        HomeCategory(Constants.SEARCH_BUSINESS, application.loadDrawable(R.drawable.business)),
        HomeCategory(Constants.SEARCH_ACCOUNTING, application.loadDrawable(R.drawable.accounting)),
        HomeCategory(
            Constants.SEARCH_IT_AND_SECURITY,
            application.loadDrawable(R.drawable.it_and_security)
        ),
        HomeCategory(
            Constants.SEARCH_GRAPHIC_DESIGN,
            application.loadDrawable(R.drawable.graphic_design)
        ),
        HomeCategory(Constants.SEARCH_MARKETING, application.loadDrawable(R.drawable.marketing))
    )

    val topSearchList = listOf(
        "python", "java", "excel", "sql",
        "javascript", "aws", "power bi", "react", "digital marketing", "c#",
        "photoshop", "sap"
    )
}