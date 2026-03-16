package com.aait.domain.usecase.general

import com.aait.domain.repository.HomeRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class CountriesUseCase(private val homeRepository: HomeRepository) {

    operator fun invoke() = flow {
        emitAll(homeRepository.countries())
    }
}