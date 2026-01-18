package com.ibeybeh.beylearn.api_test.di

import com.ibeybeh.beylearn.api_test.data.remote.datasource.TestRemoteDataSource
import com.ibeybeh.beylearn.api_test.data.remote.datasource.TestRemoteDataSourceImpl
import com.ibeybeh.beylearn.api_test.data.remote.service.TestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    internal fun provideTestRemoteDataSource(
        api: TestApi
    ): TestRemoteDataSource = TestRemoteDataSourceImpl(api)
}