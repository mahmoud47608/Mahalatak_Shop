package com.mahalatk.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
import com.mahalatk.features.packages.PackageDetailScreen
import com.mahalatk.features.packages.PackagesScreen
import com.mahalatk.features.packages.SendMessageScreen
import com.mahalatk.features.packages.UploadBannerScreen
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
import mahalatk.shared.generated.resources.phone_changed_success
import mahalatk.shared.generated.resources.registration_success
import org.jetbrains.compose.resources.stringResource

@Composable
fun NavigationHost() {
    val navigator = LocalNavigator.current

    val hasPassedAuth = remember(navigator.backStack.first()) {
        navigator.backStack.first().isTabRoute
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Tab screens — only composed after auth, then always alive
        if (hasPassedAuth) {
            MainNavGraph()
        }

        // NavDisplay handles detail screens as overlays
        NavDisplay(
            backStack = navigator.backStack,
            onBack = { navigator.pop() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = { route ->
                when (route) {
                // ─── Auth ──────────────────────────────────
                is Route.Splash -> NavEntry(route) {
                    SplashScreen(
                        onNavigateToLogin = { navigator.replaceAll(Route.Login) },
                        onNavigateToHome = { navigator.replaceAll(Route.Home) },
                    )
                }

                is Route.Login -> NavEntry(route) {
                    LoginScreen(
                        onNavigateToHome = { navigator.replaceAll(Route.Home) },
                        onNavigateToSignUp = { navigator.push(Route.Register) },
                        onNavigateToForgotPassword = { navigator.push(Route.ForgotPassword) },
                    )
                }

                is Route.Register -> NavEntry(route) {
                    RegisterScreen(
                        onNavigateToLogin = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                        onNavigateToActivation = { phone -> navigator.push(Route.Activation(phone)) },
                    )
                }

                is Route.Activation -> NavEntry(route) {
                    when {
                        route.isFromForgotPassword -> ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            onVerified = { navigator.replace(Route.ResetPassword(route.phoneNumber)) },
                        )

                        route.isFromChangePhone -> ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            headerTitle = stringResource(Res.string.change_phone_title),
                            onBack = { navigator.pop() },
                            onVerified = { navigator.replace(Route.NewPhone) },
                        )

                        route.isFromNewPhone -> ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            showSuccessOnVerify = true,
                            successMessage = stringResource(Res.string.phone_changed_success),
                            headerTitle = stringResource(Res.string.change_phone_title),
                            onBack = { navigator.pop() },
                            onVerified = { navigator.popUntil { it is Route.Settings } },
                        )

                        else -> ActivationScreen(
                            phoneNumber = route.phoneNumber,
                            showSuccessOnVerify = true,
                            successMessage = stringResource(Res.string.registration_success),
                            onVerified = { navigator.replaceAll(Route.Home) },
                        )
                    }
                }

                is Route.ForgotPassword -> NavEntry(route) {
                    ForgotPasswordScreen(
                        onBack = { navigator.pop() },
                        onSendCode = { phone ->
                            navigator.push(Route.Activation(phone, isFromForgotPassword = true))
                        },
                    )
                }

                is Route.ResetPassword -> NavEntry(route) {
                    ResetPasswordScreen(
                        onBack = { navigator.pop() },
                        onSuccess = { navigator.replaceAll(Route.Login) },
                    )
                }

                is Route.PickLocation -> NavEntry(route) {
                    PickLocationScreen(
                        onBackWithResult = { lat, lng, address ->
                            LocationResultHolder.setResult(lat, lng, address)
                            navigator.pop()
                        },
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Detail Screens ────────────────────────
                is Route.OrderDetail -> NavEntry(route) {
                    OrderDetailScreen(orderId = route.orderId, onBack = { navigator.pop() })
                }

                is Route.ChatDetail -> NavEntry(route) {
                    ChatDetailScreen(
                        chatId = route.chatId,
                        customerName = route.customerName,
                        onBack = { navigator.pop() },
                    )
                }

                is Route.Notifications -> NavEntry(route) {
                    NotificationsScreen(onBack = { navigator.pop() })
                }

                is Route.AddProduct -> NavEntry(route) {
                    AddProductScreen(onBack = { navigator.pop() })
                }

                // ─── Profile ───────────────────────────────
                is Route.Profile -> NavEntry(route) {
                    ProfileScreen(
                        onBack = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                    )
                }

                is Route.ShopOwnerProfile -> NavEntry(route) {
                    ShopOwnerProfileScreen(onBack = { navigator.pop() })
                }

                is Route.EmployeeProfile -> NavEntry(route) {
                    EmployeeProfileScreen(onBack = { navigator.pop() })
                }

                is Route.EditShopOwnerProfile -> NavEntry(route) {
                    EditShopOwnerProfileScreen(
                        onBack = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                        onChangePhone = { navigator.push(Route.ChangePhone) },
                    )
                }

                is Route.EditEmployeeProfile -> NavEntry(route) {
                    EditEmployeeProfileScreen(onBack = { navigator.pop() })
                }

                // ─── Settings ──────────────────────────────
                is Route.Settings -> NavEntry(route) {
                    SettingsScreen(
                        onBack = { navigator.pop() },
                        onChangePassword = { navigator.push(Route.ChangePassword) },
                    )
                }

                is Route.ChangePhone -> NavEntry(route) {
                    ChangePhoneScreen(
                        onBack = { navigator.pop() },
                        onConfirm = { phone ->
                            navigator.push(Route.Activation(phone, isFromChangePhone = true))
                        },
                    )
                }

                is Route.NewPhone -> NavEntry(route) {
                    NewPhoneScreen(
                        onBack = { navigator.pop() },
                        onConfirm = { newPhone ->
                            navigator.push(Route.Activation(newPhone, isFromNewPhone = true))
                        },
                    )
                }

                is Route.ChangePassword -> NavEntry(route) {
                    ChangePasswordScreen(onBack = { navigator.pop() })
                }

                // ─── More ──────────────────────────────────
                is Route.Employees -> NavEntry(route) {
                    EmployeesHubScreen(
                        onBack = { navigator.pop() },
                        onEmployeeRequests = { navigator.push(Route.EmployeeRequests) },
                        onEmployeesList = { navigator.push(Route.EmployeesList) },
                    )
                }

                is Route.EmployeeRequests -> NavEntry(route) {
                    EmployeesScreen(onBack = { navigator.pop() })
                }

                is Route.EmployeesList -> NavEntry(route) {
                    EmployeesListScreen(
                        onBack = { navigator.pop() },
                        onAddEmployee = { navigator.push(Route.AddEmployee) },
                    )
                }

                is Route.AddEmployee -> NavEntry(route) {
                    AddEmployeeScreen(onBack = { navigator.pop() })
                }

                    is Route.Packages -> NavEntry(route) {
                        PackagesScreen(
                            onBack = { navigator.pop() },
                            onPackageClick = { pkg ->
                                com.mahalatk.features.packages.PackageItemHolder.set(pkg)
                                navigator.push(Route.PackageDetail(pkg.id))
                            },
                        )
                    }

                    is Route.PackageDetail -> NavEntry(route) {
                        val item = com.mahalatk.features.packages.PackageItemHolder.get()
                            ?: com.mahalatk.features.packages.PackageItem(
                                route.packageId, "", "", "", "", "",
                                com.mahalatk.features.packages.PackageStatus.AVAILABLE,
                            )
                        PackageDetailScreen(
                            packageItem = item,
                            onBack = { navigator.pop() },
                            onUsePackage = { type ->
                                when (type) {
                                    "messages" -> navigator.push(Route.SendMessage)
                                    "home_slider", "more_slider", "cart_slider" -> navigator.push(
                                        Route.UploadBanner(type)
                                    )
                                }
                            },
                        )
                    }

                    is Route.SendMessage -> NavEntry(route) {
                        SendMessageScreen(onBack = { navigator.pop() })
                    }

                    is Route.UploadBanner -> NavEntry(route) {
                        UploadBannerScreen(
                            bannerType = route.bannerType,
                            onBack = { navigator.pop() },
                        )
                    }

                is Route.Complaints -> NavEntry(route) {
                    ComplaintsScreen(onBack = { navigator.pop() })
                }

                is Route.MyRatings -> NavEntry(route) {
                    MyRatingsScreen(onBack = { navigator.pop() })
                }

                is Route.Offers -> NavEntry(route) {
                    OffersScreen(
                        onBack = { navigator.pop() },
                        onAddOffer = { navigator.push(Route.AddOffer) },
                    )
                }

                is Route.AddOffer -> NavEntry(route) {
                    AddOfferScreen(onBack = { navigator.pop() })
                }

                is Route.Coupons -> NavEntry(route) {
                    CouponsScreen(
                        onBack = { navigator.pop() },
                        onAddCoupon = { navigator.push(Route.AddCoupon) },
                    )
                }

                is Route.AddCoupon -> NavEntry(route) {
                    AddCouponScreen(onBack = { navigator.pop() })
                }

                is Route.About -> NavEntry(route) {
                    AboutScreen(onBack = { navigator.pop() })
                }

                is Route.Terms -> NavEntry(route) {
                    TermsScreen(onBack = { navigator.pop() })
                }

                is Route.PrivacyPolicy -> NavEntry(route) {
                    PrivacyPolicyScreen(onBack = { navigator.pop() })
                }

                    // ─── Main Tabs (handled by always-alive MainNavGraph above) ──
                is Route.Home, is Route.Products, is Route.Orders,
                is Route.Chat, is Route.Account -> NavEntry(route) { }
            }
        },
        )
    }
}
