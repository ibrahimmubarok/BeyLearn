package com.ibeybeh.beylearn.api_test.di

import com.ibeybeh.beylearn.api_test.data.TestApi
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
}