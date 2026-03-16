package com.aait.base.di

import com.aait.base.ui.UIRepo
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.PreferenceRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface BaseViewModelEntryPoint {
    fun uiRepo(): UIRepo
    fun preferenceRepository(): PreferenceRepository

    fun tokenHeaderProvider(): TokenHeaderProvider
}