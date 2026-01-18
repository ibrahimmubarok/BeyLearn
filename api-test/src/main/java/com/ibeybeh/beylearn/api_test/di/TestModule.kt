package com.ibeybeh.beylearn.api_test.di

import com.ibeybeh.beylearn.api_test.data.remote.datasource.TestRemoteDataSource
import com.ibeybeh.beylearn.api_test.data.remote.service.TestApi
import com.ibeybeh.beylearn.api_test.data.repository.TestRepository
import com.ibeybeh.beylearn.api_test.data.repository.TestRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TestModule {

    @Provides
    @Singleton
    internal fun provideTestApi(retrofit: Retrofit): TestApi = retrofit.create(TestApi::class.java)

    @Provides
    @Singleton
    internal fun provideTestRepository(
        remoteDataSource: TestRemoteDataSource
    ): TestRepository = TestRepositoryImpl(remoteDataSource)
}