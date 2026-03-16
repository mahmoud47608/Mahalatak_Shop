package com.aait.domain.usecase.auth

import com.aait.domain.repository.HomeRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResendCodeUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(email: String) = flow {
        emitAll(homeRepository.resendCode(email = email))
    }
}