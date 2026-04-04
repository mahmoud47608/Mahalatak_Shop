package com.mahalatk.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mahalatk.features.auth.activation.ActivationScreen
import com.mahalatk.features.auth.forgotpassword.ForgotPasswordScreen
import com.mahalatk.features.auth.forgotpassword.ResetPasswordScreen
import com.mahalatk.features.auth.login.LoginScreen
import com.mahalatk.features.auth.register.LocationResultHolder
import com.mahalatk.features.auth.register.PickLocationScreen
import com.mahalatk.features.auth.register.RegisterScreen
import com.mahalatk.features.chat.ChatDetailScreen
import com.mahalatk.features.complaints.ComplaintsScreen
import com.mahalatk.features.coupons.AddCouponScreen
import com.mahalatk.features.coupons.CouponsScreen
import com.mahalatk.features.employees.AddEmployeeScreen
import com.mahalatk.features.employees.EmployeesHubScreen
import com.mahalatk.features.employees.EmployeesListScreen
import com.mahalatk.features.employees.EmployeesScreen
import com.mahalatk.features.more.AboutScreen
import com.mahalatk.features.more.PrivacyPolicyScreen
import com.mahalatk.features.more.TermsScreen
import com.mahalatk.features.notifications.NotificationsScreen
import com.mahalatk.features.offers.OffersScreen
import com.mahalatk.features.offers.add.AddOfferScreen
import com.mahalatk.features.orders.detail.OrderDetailScreen
import com.mahalatk.features.products.add.AddProductScreen
import com.mahalatk.features.profile.ProfileScreen
import com.mahalatk.features.profile.employee.EditEmployeeProfileScreen
import com.mahalatk.features.profile.employee.EmployeeProfileScreen
import com.mahalatk.features.profile.shopowner.EditShopOwnerProfileScreen
import com.mahalatk.features.profile.shopowner.ShopOwnerProfileScreen
import com.mahalatk.features.ratings.MyRatingsScreen
import com.mahalatk.features.settings.SettingsScreen
import com.mahalatk.features.settings.changepassword.ChangePasswordScreen
import com.mahalatk.features.settings.changephone.ChangePhoneScreen
import com.mahalatk.features.settings.changephone.NewPhoneScreen
import com.mahalatk.features.splash.SplashScreen
import com.mahalatk.navigation.graphs.MainNavGraph
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.change_phone_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavigationHost() {
    val navigator = LocalNavigator.current

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.pop() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = { route ->
            when (route) {
                is Route.Splash, is Route.Login, is Route.Register,
                is Route.Activation, is Route.ForgotPassword,
                is Route.ResetPassword, is Route.PickLocation,
                    -> authEntry(route, navigator)

                is Route.OrderDetail, is Route.ChatDetail,
                is Route.Notifications, is Route.AddProduct,
                    -> detailEntry(route, navigator)

                is Route.Profile, is Route.ShopOwnerProfile,
                is Route.EmployeeProfile, is Route.EditShopOwnerProfile,
                is Route.EditEmployeeProfile,
                    -> profileEntry(route, navigator)

                is Route.Settings, is Route.ChangePhone,
                is Route.NewPhone, is Route.ChangePassword,
                    -> settingsEntry(route, navigator)

                is Route.Employees, is Route.EmployeeRequests,
                is Route.EmployeesList, is Route.AddEmployee,
                is Route.Complaints, is Route.MyRatings,
                is Route.Offers, is Route.AddOffer,
                is Route.Coupons, is Route.AddCoupon,
                is Route.About, is Route.Terms, is Route.PrivacyPolicy,
                    -> moreEntry(route, navigator)

                is Route.Home, is Route.Products, is Route.Orders,
                is Route.Chat, is Route.Account,
                    -> NavEntry(route) { MainNavGraph() }
            }
        },
    )
}

// ═══════════════════════════════════════════════════════
// Auth
// ═══════════════════════════════════════════════════════

private fun authEntry(route: Route, nav: AppNavigator): NavEntry<Route> = when (route) {
    is Route.Splash -> NavEntry(route) {
        SplashScreen(
            onNavigateToLogin = { nav.replaceAll(Route.Login) },
            onNavigateToHome = { nav.replaceAll(Route.Home) },
        )
    }

    is Route.Login -> NavEntry(route) {
        LoginScreen(
            onNavigateToHome = { nav.replaceAll(Route.Home) },
            onNavigateToSignUp = { nav.push(Route.Register) },
            onNavigateToForgotPassword = { nav.push(Route.ForgotPassword) },
        )
    }

    is Route.Register -> NavEntry(route) {
        RegisterScreen(
            onNavigateToLogin = { nav.pop() },
            onNavigateToPickLocation = { nav.push(Route.PickLocation) },
            onNavigateToActivation = { phone -> nav.push(Route.Activation(phone)) },
        )
    }

    is Route.Activation -> NavEntry(route) {
        when {
            route.isFromForgotPassword -> ActivationScreen(
                phoneNumber = route.phoneNumber,
                onVerified = { nav.replace(Route.ResetPassword(route.phoneNumber)) },
            )

            route.isFromChangePhone -> ActivationScreen(
                phoneNumber = route.phoneNumber,
                headerTitle = stringResource(Res.string.change_phone_title),
                onBack = { nav.pop() },
                onVerified = { nav.replace(Route.NewPhone) },
            )

            route.isFromNewPhone -> ActivationScreen(
                phoneNumber = route.phoneNumber,
                showSuccessOnVerify = true,
                successMessage = "Phone number changed successfully!",
                headerTitle = stringResource(Res.string.change_phone_title),
                onBack = { nav.pop() },
                onVerified = { nav.popUntil { it is Route.Settings } },
            )

            else -> ActivationScreen(
                phoneNumber = route.phoneNumber,
                showSuccessOnVerify = true,
                successMessage = "Registration request sent successfully!",
                onVerified = { nav.replaceAll(Route.Home) },
            )
        }
    }

    is Route.ForgotPassword -> NavEntry(route) {
        ForgotPasswordScreen(
            onSendCode = { phone ->
                nav.push(Route.Activation(phone, isFromForgotPassword = true))
            },
        )
    }

    is Route.ResetPassword -> NavEntry(route) {
        ResetPasswordScreen(onSuccess = { nav.replaceAll(Route.Login) })
    }

    is Route.PickLocation -> NavEntry(route) {
        PickLocationScreen(
            onBackWithResult = { lat, lng, address ->
                LocationResultHolder.setResult(lat, lng, address)
                nav.pop()
            },
            onBack = { nav.pop() },
        )
    }

    else -> error("Unknown auth route: $route")
}

// ═══════════════════════════════════════════════════════
// Detail Screens
// ═══════════════════════════════════════════════════════

private fun detailEntry(route: Route, nav: AppNavigator): NavEntry<Route> = when (route) {
    is Route.OrderDetail -> NavEntry(route) {
        OrderDetailScreen(orderId = route.orderId, onBack = { nav.pop() })
    }

    is Route.ChatDetail -> NavEntry(route) {
        ChatDetailScreen(
            chatId = route.chatId,
            customerName = route.customerName,
            onBack = { nav.pop() },
        )
    }

    is Route.Notifications -> NavEntry(route) {
        NotificationsScreen(onBack = { nav.pop() })
    }

    is Route.AddProduct -> NavEntry(route) {
        AddProductScreen(onBack = { nav.pop() })
    }

    else -> error("Unknown detail route: $route")
}

// ═══════════════════════════════════════════════════════
// Profile
// ═══════════════════════════════════════════════════════

private fun profileEntry(route: Route, nav: AppNavigator): NavEntry<Route> = when (route) {
    is Route.Profile -> NavEntry(route) {
        ProfileScreen(
            onBack = { nav.pop() },
            onNavigateToPickLocation = { nav.push(Route.PickLocation) },
        )
    }

    is Route.ShopOwnerProfile -> NavEntry(route) {
        ShopOwnerProfileScreen(onBack = { nav.pop() })
    }

    is Route.EmployeeProfile -> NavEntry(route) {
        EmployeeProfileScreen(onBack = { nav.pop() })
    }

    is Route.EditShopOwnerProfile -> NavEntry(route) {
        EditShopOwnerProfileScreen(
            onBack = { nav.pop() },
            onNavigateToPickLocation = { nav.push(Route.PickLocation) },
        )
    }

    is Route.EditEmployeeProfile -> NavEntry(route) {
        EditEmployeeProfileScreen(onBack = { nav.pop() })
    }

    else -> error("Unknown profile route: $route")
}

// ═══════════════════════════════════════════════════════
// Settings
// ═══════════════════════════════════════════════════════

private fun settingsEntry(route: Route, nav: AppNavigator): NavEntry<Route> = when (route) {
    is Route.Settings -> NavEntry(route) {
        SettingsScreen(
            onBack = { nav.pop() },
            onEditProfile = { nav.push(Route.EditShopOwnerProfile) },
            onChangePhoneNumber = { nav.push(Route.ChangePhone) },
            onChangePassword = { nav.push(Route.ChangePassword) },
        )
    }

    is Route.ChangePhone -> NavEntry(route) {
        ChangePhoneScreen(
            onBack = { nav.pop() },
            onConfirm = { phone ->
                nav.push(Route.Activation(phone, isFromChangePhone = true))
            },
        )
    }

    is Route.NewPhone -> NavEntry(route) {
        NewPhoneScreen(
            onBack = { nav.pop() },
            onConfirm = { newPhone ->
                nav.push(Route.Activation(newPhone, isFromNewPhone = true))
            },
        )
    }

    is Route.ChangePassword -> NavEntry(route) {
        ChangePasswordScreen(onBack = { nav.pop() })
    }

    else -> error("Unknown settings route: $route")
}

// ═══════════════════════════════════════════════════════
// More (Employees, Complaints, Offers, Coupons, etc.)
// ═══════════════════════════════════════════════════════

private fun moreEntry(route: Route, nav: AppNavigator): NavEntry<Route> = when (route) {
    is Route.Employees -> NavEntry(route) {
        EmployeesHubScreen(
            onBack = { nav.pop() },
            onEmployeeRequests = { nav.push(Route.EmployeeRequests) },
            onEmployeesList = { nav.push(Route.EmployeesList) },
        )
    }

    is Route.EmployeeRequests -> NavEntry(route) {
        EmployeesScreen(onBack = { nav.pop() })
    }

    is Route.EmployeesList -> NavEntry(route) {
        EmployeesListScreen(
            onBack = { nav.pop() },
            onAddEmployee = { nav.push(Route.AddEmployee) },
        )
    }

    is Route.AddEmployee -> NavEntry(route) {
        AddEmployeeScreen(onBack = { nav.pop() })
    }

    is Route.Complaints -> NavEntry(route) {
        ComplaintsScreen(onBack = { nav.pop() })
    }

    is Route.MyRatings -> NavEntry(route) {
        MyRatingsScreen(onBack = { nav.pop() })
    }

    is Route.Offers -> NavEntry(route) {
        OffersScreen(
            onBack = { nav.pop() },
            onAddOffer = { nav.push(Route.AddOffer) },
        )
    }

    is Route.AddOffer -> NavEntry(route) {
        AddOfferScreen(onBack = { nav.pop() })
    }

    is Route.Coupons -> NavEntry(route) {
        CouponsScreen(
            onBack = { nav.pop() },
            onAddCoupon = { nav.push(Route.AddCoupon) },
        )
    }

    is Route.AddCoupon -> NavEntry(route) {
        AddCouponScreen(onBack = { nav.pop() })
    }

    is Route.About -> NavEntry(route) {
        AboutScreen(onBack = { nav.pop() })
    }

    is Route.Terms -> NavEntry(route) {
        TermsScreen(onBack = { nav.pop() })
    }

    is Route.PrivacyPolicy -> NavEntry(route) {
        PrivacyPolicyScreen(onBack = { nav.pop() })
    }

    else -> error("Unknown more route: $route")
}
