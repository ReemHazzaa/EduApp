package com.reemmousa.eduapp.dataSources.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.reemmousa.eduapp.R
import com.reemmousa.eduapp.app.Constants.COURSE_PRICE_FREE
import com.reemmousa.eduapp.app.Constants.DEFAULT_COURSE_CATEGORY
import com.reemmousa.eduapp.app.Constants.PREFS
import com.reemmousa.eduapp.app.Constants.PREF_CATEGORY
import com.reemmousa.eduapp.app.Constants.PREF_CATEGORY_ID
import com.reemmousa.eduapp.app.Constants.PREF_PRICE
import com.reemmousa.eduapp.app.Constants.PREF_PRICE_ID
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo.PreferenceKeys.selectedCourseCategory
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo.PreferenceKeys.selectedCourseCategoryId
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo.PreferenceKeys.selectedCoursePrice
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo.PreferenceKeys.selectedCoursePriceId
import com.reemmousa.eduapp.dataStructures.filter.FilterPrefs
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepo @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS)
    private val preferences = context.dataStore.data

    private object PreferenceKeys {
        val selectedCoursePrice = stringPreferencesKey(PREF_PRICE)
        val selectedCoursePriceId = intPreferencesKey(PREF_PRICE_ID)
        val selectedCourseCategory = stringPreferencesKey(PREF_CATEGORY)
        val selectedCourseCategoryId = intPreferencesKey(PREF_CATEGORY_ID)

    }

    suspend fun savePrefs(filterPrefs: FilterPrefs) {
        context.dataStore.edit { prefs ->
            prefs[selectedCoursePrice] = filterPrefs.selectedCoursePrice
            prefs[selectedCoursePriceId] = filterPrefs.selectedCoursePriceId
            prefs[selectedCourseCategory] = filterPrefs.selectedCourseCategory
            prefs[selectedCourseCategoryId] = filterPrefs.selectedCourseCategoryId
        }
    }

    val readPrefs: Flow<FilterPrefs> =
        context.dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val selectedCoursePrice =
                preferences[PreferenceKeys.selectedCoursePrice] ?: COURSE_PRICE_FREE
            val selectedCoursePriceId =
                preferences[PreferenceKeys.selectedCoursePriceId] ?: 0
            val selectedCourseCategory =
                preferences[PreferenceKeys.selectedCourseCategory] ?: DEFAULT_COURSE_CATEGORY
            val selectedCourseCategoryId =
                preferences[PreferenceKeys.selectedCourseCategoryId] ?: 0
            FilterPrefs(
                selectedCoursePrice,
                selectedCoursePriceId,
                selectedCourseCategory,
                selectedCourseCategoryId
            )
        }

    suspend fun saveSelectedCoursePrice(price: String) {
        context.dataStore.edit { preferences ->
            preferences[selectedCoursePrice] = price
        }
    }

    suspend fun readSelectedCoursePrice(): String {
        return preferences.first()[selectedCoursePrice] ?: COURSE_PRICE_FREE
    }

    suspend fun saveSelectedCoursePriceId(priceId: Int) {
        context.dataStore.edit { preferences ->
            preferences[selectedCoursePriceId] = priceId
        }
    }

    suspend fun readSelectedCoursePriceId(): Int {
        return preferences.first()[selectedCoursePriceId] ?: 0
    }

    suspend fun saveSelectedCourseCategory(cat: String) {
        context.dataStore.edit { preferences ->
            preferences[selectedCourseCategory] = cat
        }
    }

    suspend fun readSelectedCourseCategory(): String {
        return preferences.first()[selectedCourseCategory] ?: DEFAULT_COURSE_CATEGORY
    }

    suspend fun saveSelectedCourseCategoryId(priceId: Int) {
        context.dataStore.edit { preferences ->
            preferences[selectedCourseCategoryId] = priceId
        }
    }

    suspend fun readSelectedCourseCategoryId(): Int {
        return preferences.first()[selectedCourseCategoryId] ?: 0
    }
}