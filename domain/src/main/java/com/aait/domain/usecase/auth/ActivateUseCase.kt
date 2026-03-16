package com.aait.domain.usecase.auth

import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.HomeRepository
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ActivateUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(
        code: String,
        email: String,
        deviceId: String,
        macAddress: String
    ) = flow {
        when {
            code.length != 4 -> emit(DataState.Error(ValidationException.InValidCodeException))
            else -> emitAll(
                homeRepository.activate(
                    code = code,
                    email = email,
                    deviceId = deviceId,
                    macAddress = macAddress
                )
            )
        }
    }
}