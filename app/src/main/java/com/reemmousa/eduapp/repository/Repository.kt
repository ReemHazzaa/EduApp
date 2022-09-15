package com.reemmousa.eduapp.repository

import com.reemmousa.eduapp.dataSources.constants.ConstantsDataSource
import com.reemmousa.eduapp.dataSources.dataStore.DataStoreRepo
import com.reemmousa.eduapp.dataSources.local.LocalDataSource
import com.reemmousa.eduapp.dataSources.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource,
    constantsDataSource: ConstantsDataSource,
    dataStoreRepo: DataStoreRepo
) {
    val remote = remoteDataSource
    val constants = constantsDataSource
    val dataStoreRepository = dataStoreRepo
    val local = localDataSource
}