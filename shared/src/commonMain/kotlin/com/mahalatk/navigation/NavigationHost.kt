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
import com.mahalatk.features.employees.AddEmployeeScreen
import com.mahalatk.features.employees.EmployeesHubScreen
import com.mahalatk.features.employees.EmployeesListScreen
import com.mahalatk.features.employees.EmployeesScreen
import com.mahalatk.features.more.AboutScreen
import com.mahalatk.features.more.PrivacyPolicyScreen
import com.mahalatk.features.more.TermsScreen
import com.mahalatk.features.notifications.NotificationsScreen
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
                // ─── Auth ────────────────────────────────
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
                        onNavigateToActivation = { phone ->
                            navigator.push(Route.Activation(phone))
                        },
                    )
                }

                is Route.Activation -> NavEntry(route) {
                    when {
                        route.isFromForgotPassword -> {
                            ActivationScreen(
                                phoneNumber = route.phoneNumber,
                                onVerified = {
                                    navigator.replace(Route.ResetPassword(route.phoneNumber))
                                },
                            )
                        }

                        route.isFromChangePhone -> {
                            // Verified current phone → go to enter new phone
                            ActivationScreen(
                                phoneNumber = route.phoneNumber,
                                headerTitle = stringResource(Res.string.change_phone_title),
                                onBack = { navigator.pop() },
                                onVerified = {
                                    navigator.replace(Route.NewPhone)
                                },
                            )
                        }

                        route.isFromNewPhone -> {
                            // Verified new phone → show success then go back to settings
                            ActivationScreen(
                                phoneNumber = route.phoneNumber,
                                showSuccessOnVerify = true,
                                successMessage = "Phone number changed successfully!",
                                headerTitle = stringResource(Res.string.change_phone_title),
                                onBack = { navigator.pop() },
                                onVerified = {
                                    // Pop back to settings
                                    navigator.popUntil { it is Route.Settings }
                                },
                            )
                        }

                        else -> {
                            // Registration flow → show success then go Home
                            ActivationScreen(
                                phoneNumber = route.phoneNumber,
                                showSuccessOnVerify = true,
                                successMessage = "Registration request sent successfully!",
                                onVerified = { navigator.replaceAll(Route.Home) },
                            )
                        }
                    }
                }

                // ─── Forgot Password Flow ────────────────
                is Route.ForgotPassword -> NavEntry(route) {
                    ForgotPasswordScreen(
                        onSendCode = { phone ->
                            navigator.push(
                                Route.Activation(phone, isFromForgotPassword = true)
                            )
                        },
                    )
                }

                is Route.ResetPassword -> NavEntry(route) {
                    ResetPasswordScreen(
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

                // ─── Notifications ───────────────────────
                is Route.Notifications -> NavEntry(route) {
                    NotificationsScreen(onBack = { navigator.pop() })
                }

                // ─── Order Detail ───────────────────────
                is Route.OrderDetail -> NavEntry(route) {
                    com.mahalatk.features.orders.detail.OrderDetailScreen(
                        orderId = route.orderId,
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Chat Detail ─────────────────────────
                is Route.ChatDetail -> NavEntry(route) {
                    ChatDetailScreen(
                        chatId = route.chatId,
                        customerName = route.customerName,
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Add Product ────────────────────────
                is Route.AddProduct -> NavEntry(route) {
                    AddProductScreen(onBack = { navigator.pop() })
                }

                // ─── Edit Profile (Unified with tabs) ────────
                is Route.Profile -> NavEntry(route) {
                    ProfileScreen(
                        onBack = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                    )
                }

                // ─── Shop Owner Profile (read-only) ──────────
                is Route.ShopOwnerProfile -> NavEntry(route) {
                    ShopOwnerProfileScreen(
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Employee Profile (read-only) ────────────
                is Route.EmployeeProfile -> NavEntry(route) {
                    EmployeeProfileScreen(
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Edit Shop Owner Profile ────────────────
                is Route.EditShopOwnerProfile -> NavEntry(route) {
                    EditShopOwnerProfileScreen(
                        onBack = { navigator.pop() },
                        onNavigateToPickLocation = { navigator.push(Route.PickLocation) },
                    )
                }

                // ─── Edit Employee Profile ──────────────────
                is Route.EditEmployeeProfile -> NavEntry(route) {
                    EditEmployeeProfileScreen(
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Change Phone ─────────────────────────
                is Route.ChangePhone -> NavEntry(route) {
                    ChangePhoneScreen(
                        onBack = { navigator.pop() },
                        onConfirm = { phone ->
                            navigator.push(Route.Activation(phone, isFromChangePhone = true))
                        },
                    )
                }

                // ─── New Phone ────────────────────────────
                is Route.NewPhone -> NavEntry(route) {
                    NewPhoneScreen(
                        onBack = { navigator.pop() },
                        onConfirm = { newPhone ->
                            navigator.push(Route.Activation(newPhone, isFromNewPhone = true))
                        },
                    )
                }

                // ─── Change Password ─────────────────────
                is Route.ChangePassword -> NavEntry(route) {
                    ChangePasswordScreen(
                        onBack = { navigator.pop() },
                    )
                }

                // ─── Settings ─────────────────────────────
                is Route.Settings -> NavEntry(route) {
                    SettingsScreen(
                        onBack = { navigator.pop() },
                        onEditProfile = { navigator.push(Route.EditShopOwnerProfile) },
                        onChangePhoneNumber = { navigator.push(Route.ChangePhone) },
                        onChangePassword = { navigator.push(Route.ChangePassword) },
                    )
                }

                // ─── My Ratings ──────────────────────────
                is Route.MyRatings -> NavEntry(route) {
                    MyRatingsScreen(onBack = { navigator.pop() })
                }

                // ─── Employees Hub ───────────────────────
                is Route.Employees -> NavEntry(route) {
                    EmployeesHubScreen(
                        onBack = { navigator.pop() },
                        onEmployeeRequests = { navigator.push(Route.EmployeeRequests) },
                        onEmployeesList = { navigator.push(Route.EmployeesList) },
                    )
                }

                // ─── Employee Requests (applicants) ─────
                is Route.EmployeeRequests -> NavEntry(route) {
                    EmployeesScreen(onBack = { navigator.pop() })
                }

                // ─── Employees List ─────────────────────
                is Route.EmployeesList -> NavEntry(route) {
                    EmployeesListScreen(
                        onBack = { navigator.pop() },
                        onAddEmployee = { navigator.push(Route.AddEmployee) },
                    )
                }

                // ─── Add Employee ───────────────────────
                is Route.AddEmployee -> NavEntry(route) {
                    AddEmployeeScreen(onBack = { navigator.pop() })
                }

                // ─── About ───────────────────────────────
                is Route.About -> NavEntry(route) {
                    AboutScreen(onBack = { navigator.pop() })
                }

                // ─── Terms & Conditions ──────────────────
                is Route.Terms -> NavEntry(route) {
                    TermsScreen(onBack = { navigator.pop() })
                }

                // ─── Privacy Policy ──────────────────────
                is Route.PrivacyPolicy -> NavEntry(route) {
                    PrivacyPolicyScreen(onBack = { navigator.pop() })
                }

                // ─── Main Tabs ───────────────────────────
                is Route.Home, is Route.Products, is Route.Orders, is Route.Chat,
                is Route.Account -> NavEntry(route) {
                    MainNavGraph(route = route)
                }
            }
        }
    )
}
