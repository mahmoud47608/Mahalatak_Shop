package com.mahalatk.di

import com.mahalatk.base.UserDataProvider
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.base.managers.SessionManager
import com.mahalatk.fcm.FcmEventHandler
import com.mahalatk.features.auth.activation.ActivationViewModel
import com.mahalatk.features.auth.forgotpassword.ForgotPasswordViewModel
import com.mahalatk.features.auth.forgotpassword.ResetPasswordViewModel
import com.mahalatk.features.auth.login.LoginViewModel
import com.mahalatk.features.auth.register.RegisterViewModel
import com.mahalatk.features.chat.ChatDetailViewModel
import com.mahalatk.features.chat.ChatViewModel
import com.mahalatk.features.home.HomeViewModel
import com.mahalatk.features.main.MainViewModel
import com.mahalatk.features.more.MoreViewModel
import com.mahalatk.features.notifications.NotificationsViewModel
import com.mahalatk.features.orders.OrdersViewModel
import com.mahalatk.features.orders.detail.OrderDetailViewModel
import com.mahalatk.features.products.ProductsViewModel
import com.mahalatk.features.products.add.AddProductViewModel
import com.mahalatk.features.profile.employee.EmployeeProfileViewModel
import com.mahalatk.features.profile.shopowner.ShopOwnerProfileViewModel
import com.mahalatk.features.settings.changepassword.ChangePasswordViewModel
import com.mahalatk.features.settings.changephone.ChangePhoneViewModel
import com.mahalatk.features.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {

    // ─── Managers (singletons) ──────────
    single { LoadingManager() }
    single { MessageManager() }
    single { SessionManager() }
    single { FcmEventHandler(get(), get()) }
    single { UserDataProvider(get()) }

    // ─── ViewModels ─────────────────────
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MoreViewModel(get(), get(), get()) }
    viewModel { ProductsViewModel() }
    viewModel { AddProductViewModel(get(), get(), get(), get()) }
    viewModel { OrdersViewModel() }
    viewModel { ChatViewModel() }
    viewModel { ChatDetailViewModel() }
    viewModel { NotificationsViewModel() }
    viewModel { ActivationViewModel() }
    viewModel { ForgotPasswordViewModel() }
    viewModel { ResetPasswordViewModel() }
    viewModel { OrderDetailViewModel() }
    viewModel { ShopOwnerProfileViewModel(get(), get(), get()) }
    viewModel { EmployeeProfileViewModel(get(), get(), get()) }
    viewModel { ChangePhoneViewModel(get(), get()) }
    viewModel { ChangePasswordViewModel(get(), get()) }
}
