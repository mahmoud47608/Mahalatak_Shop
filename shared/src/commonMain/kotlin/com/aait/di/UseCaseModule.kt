package com.aait.di

import com.aait.domain.usecase.auth.ActivateUseCase
import com.aait.domain.usecase.auth.ForgetPasswordUseCase
import com.aait.domain.usecase.auth.LoginUseCase
import com.aait.domain.usecase.auth.RegisterUseCase
import com.aait.domain.usecase.auth.ResendCodeUseCase
import com.aait.domain.usecase.auth.ResetPasswordUseCase
import com.aait.domain.usecase.chat.MessagesUseCase
import com.aait.domain.usecase.chat.UploadMessageFileUseCase
import com.aait.domain.usecase.general.CountriesUseCase
import com.aait.domain.usecase.socket.ConnectSocketUseCase
import com.aait.domain.usecase.socket.DisconnectSocketUseCase
import com.aait.domain.usecase.socket.EmitSocketUseCase
import com.aait.domain.usecase.socket.OpenChannelSocketUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }
    factory { ActivateUseCase(get()) }
    factory { ForgetPasswordUseCase(get()) }
    factory { ResetPasswordUseCase(get()) }
    factory { ResendCodeUseCase(get()) }
    factory { CountriesUseCase(get()) }
    factory { MessagesUseCase(get()) }
    factory { UploadMessageFileUseCase(get()) }
    factory { ConnectSocketUseCase(get()) }
    factory { DisconnectSocketUseCase(get()) }
    factory { EmitSocketUseCase(get()) }
    factory { OpenChannelSocketUseCase(get()) }
}
