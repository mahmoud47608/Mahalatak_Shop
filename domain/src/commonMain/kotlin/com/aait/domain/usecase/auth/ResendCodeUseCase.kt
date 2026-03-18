package com.aait.domain.usecase.auth

import com.aait.domain.repository.HomeRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ResendCodeUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke(email: String) = flow {
        emitAll(homeRepository.resendCode(email = email))
    }
}