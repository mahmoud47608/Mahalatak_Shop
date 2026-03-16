package com.aait.domain.usecase.general

import com.aait.domain.repository.HomeRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CountriesUseCase @Inject constructor(private val homeRepository: HomeRepository) {

    operator fun invoke() = flow {
        emitAll(homeRepository.countries())
    }
}