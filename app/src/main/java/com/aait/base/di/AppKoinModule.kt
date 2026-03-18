package com.aait.base.di

import com.aait.base.MainViewModel
import com.aait.base.common.screens.general.terms.TermsViewModel
import com.aait.base.cycles.auth.login.LoginViewModel
import com.aait.base.cycles.splash.SplashViewModel
import com.aait.base.fcm.NotificationHandler
import com.aait.base.ui.UIRepo
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
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
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

val appModule = module {
    single { UIRepo() }
    single { NotificationHandler(androidContext(), get(), get()) }

    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { SplashViewModel() }
    viewModel { TermsViewModel() }
}
