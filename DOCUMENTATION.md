# Mahalatak Shop- Complete Project Guide

## Compose Multiplatform (CMP) + Clean Architecture

---

## Table of Contents

1. [Introduction - What are KMP and CMP?](#1-introduction)
2. [Overall Project Structure](#2-overall-project-structure)
3. [The Four Modules](#3-the-four-modules)
4. [The expect/actual Concept](#4-the-expectactual-concept)
5. [Source Sets - What Are They?](#5-source-sets)
6. [Domain Module - The Business Layer](#6-domain-module)
7. [Data Module - The Data Layer](#7-data-module)
8. [Shared Module - The UI Layer](#8-shared-module)
9. [App Module - Android Entry Point](#9-app-module)
10. [Dependency Injection with Koin](#10-dependency-injection-with-koin)
11. [The Navigation System](#11-navigation-system)
12. [The Network Layer (Ktor)](#12-network-layer)
13. [State Management](#13-state-management)
14. [Theme and Design System](#14-theme-and-design-system)
15. [Firebase and Push Notifications](#15-firebase-and-push-notifications)
16. [How Does iOS Work?](#16-how-does-ios-work)
17. [Build and Configuration Files](#17-build-files)
18. [Complete Flow: From Pressing Login to the Response](#18-complete-flow)
19. [Libraries Used](#19-libraries-used)
20. [Important Tips and Concepts](#20-important-tips-and-concepts)

---

## 1. Introduction

### What is KMP (Kotlin Multiplatform)?

KMP is a technology from JetBrains that lets you write Kotlin code **once** and have it run on more
than one platform (Android + iOS).

**The idea, simply put:**

- Shared code (Business Logic, Network, Models) → you write it once in `commonMain`
- Platform-specific code (Camera, GPS, Encryption) → you write it in `androidMain` or `iosMain`

### What is CMP (Compose Multiplatform)?

CMP is an extension of Jetpack Compose so it can run on iOS as well. That means instead of writing
UI with Compose for Android and SwiftUI for iOS, you write Compose **once** and both share the same
UI.

### Why Use Them?

```
Without KMP:                      With KMP:
┌──────────┐ ┌──────────┐         ┌──────────────────┐
│ Android  │ │   iOS    │         │   commonMain     │
│ (Kotlin) │ │ (Swift)  │         │  (shared code)   │
│          │ │          │         ├────────┬─────────┤
│ UI       │ │ UI       │         │Android │  iOS    │
│ Network  │ │ Network  │         │ Main   │  Main   │
│ Logic    │ │ Logic    │         │(specfc)│(specfc) │
│ Models   │ │ Models   │         └────────┴─────────┘
└──────────┘ └──────────┘
  code × 2                          code × 1
```

---

## 2. Overall Project Structure

```
Mahalatak/
├── app/                    ← Android entry point (Application + Activity)
├── data/                   ← Data layer (API + Storage) - KMP Module
│   └── src/
│       ├── commonMain/     ← shared code (Ktor endpoints, Repositories, Utils)
│       ├── androidMain/    ← Android-only code (OkHttp, EncryptedSharedPreferences)
│       └── iosMain/        ← iOS-only code (Darwin engine, Settings)
├── domain/                 ← Business layer (Entities, Use Cases, Interfaces) - KMP Module
│   └── src/
│       ├── commonMain/     ← shared code (Models, Interfaces, Validation)
│       ├── androidMain/    ← Android-only code (Locale)
│       └── iosMain/        ← iOS-only code (NSLocale)
├── shared/                 ← UI layer (Compose Multiplatform) - KMP Module
│   └── src/
│       ├── commonMain/     ← shared code (Screens, ViewModels, Theme, Navigation)
│       ├── androidMain/    ← Android-only code (Firebase, Notifications, Activity)
│       └── iosMain/        ← iOS-only code (MainViewController, KoinInit)
├── gradle/
│   └── libs.versions.toml  ← file for managing dependencies and their versions
├── build.gradle.kts         ← Root build file
├── settings.gradle.kts      ← module definitions
└── Config                   ← API keys and URLs
```

---

## 3. The Four Modules

The project is built on **Clean Architecture** and split into 4 modules:

```
┌─────────────────────────────────────────────┐
│                    app                       │
│         (Android Entry Point)                │
│              depends on: shared              │
├─────────────────────────────────────────────┤
│                  shared                      │
│    (UI Layer - Compose Multiplatform)        │
│         depends on: domain + data            │
├──────────────────────┬──────────────────────┤
│        data          │       domain          │
│   (Data Layer)       │   (Business Layer)    │
│  depends on: domain  │   independent - does  │
│                      │   not depend on any   │
│                      │   other module        │
└──────────────────────┴──────────────────────┘
```

### The Golden Rule:

- **domain** depends on no one ← completely independent
- **data** depends on domain ← so it can implement the interfaces
- **shared** depends on domain + data ← so it can use everything
- **app** depends on shared ← Android entry point only

### Why This Order?

Because if you change how data is stored (e.g., from API to Firebase) → you only change data.
If you change the UI (e.g., a redesign) → you only change shared.
The domain stays stable because it is the business logic.

---

## 4. The expect/actual Concept

This is one of the most important concepts in KMP. When you have **shared** code that needs a *
*different** implementation for each platform:

### How Does It Work?

```kotlin
// ──── commonMain (the declaration - "I need this thing") ────
expect fun getPlatformLanguage(): String

// ──── androidMain (the implementation for Android) ────
actual fun getPlatformLanguage(): String {
    return java.util.Locale.getDefault().language  // uses the Java API
}

// ──── iosMain (the implementation for iOS) ────
actual fun getPlatformLanguage(): String {
    return NSLocale.currentLocale.languageCode ?: "en"  // uses the iOS API
}
```

### Examples from the Project:

| expect (commonMain)                     | Android actual           | iOS actual               |
|-----------------------------------------|--------------------------|--------------------------|
| `expect val platformDeviceType: String` | `"android"`              | `"ios"`                  |
| `expect object AppConfig`               | reads from `BuildConfig` | hardcoded values         |
| `expect fun createPlatformHttpClient()` | `HttpClient(OkHttp)`     | `HttpClient(Darwin)`     |
| `expect fun getPlatformLanguage()`      | `Locale.getDefault()`    | `NSLocale.currentLocale` |

### When Do You Use expect/actual?

- When you need a platform-specific API (Locale, Keychain, Context)
- When you need a different library for each platform (OkHttp vs Darwin)
- When you need a different config (BuildConfig vs hardcoded)

---

## 5. Source Sets - What Are They?

Every KMP module has **source sets** - that is, separate groups of code:

```
data/src/
├── commonMain/    ← code that runs on all platforms
│                    (Pure Kotlin + Multiplatform Libraries)
│
├── androidMain/   ← code that runs on Android only
│                    (Android APIs + Java Libraries)
│
├── iosMain/       ← code that runs on iOS only
│                    (iOS APIs + Objective-C/Swift Interop)
│
└── commonTest/    ← shared tests
```

### The Rules:

1. `commonMain` **cannot** use any Android or iOS APIs
2. `androidMain` can use everything in `commonMain` + Android APIs
3. `iosMain` can use everything in `commonMain` + iOS APIs
4. `androidMain` and `iosMain` **cannot** see each other

```
         commonMain
        /          \
androidMain      iosMain
(sees common)   (sees common)
(can't see iOS) (can't see Android)
```

---

## 6. Domain Module - The Business Layer

The domain is the **heart of the app** - it holds the business logic and rules. It does not depend
on any external framework or library (except Coroutines and Serialization).

### File Structure:

```
domain/src/commonMain/kotlin/com/mahalatk/domain/
├── entity/                    ← the Data Models
│   ├── AuthData.kt            ← user data after Login
│   ├── LatLngModel.kt         ← location data
│   └── base/
│       ├── BaseResponse.kt    ← the Response wrapper from the server
│       └── AnyResponse.kt     ← empty Response
│
├── repository/                ← the Interfaces (the contracts)
│   ├── HomeRepository.kt      ← contract for Home operations (login)
│   └── PreferenceRepository.kt ← contract for managing Preferences
│
├── usecase/auth/              ← the Use Cases (the business logic)
│   └── LoginUseCase.kt        ← the Login logic (validation + call)
│
├── exception/                 ← the custom Exceptions
│   └── ValidationException.kt ← validation errors (Phone, Password)
│
└── util/                      ← helper utilities
    ├── Constants.kt           ← constants (API param names, error codes)
    ├── DataState.kt           ← data states (Success, Error, Loading, Idle)
    ├── NetworkExceptions.kt   ← network errors (Timeout, Auth, Server, etc.)
    ├── CommonValidation.kt    ← validation for Phone and Password
    ├── Gson.kt                ← JSON conversion functions (toJson, fromJson)
    ├── TokenCacheManager.kt   ← Interface for managing the Token cache
    ├── PlatformLanguage.kt    ← expect function for the device language
    └── ResponseStatusConstants.kt ← Response status constants
```

### Explanation of the Important Files:

#### DataState.kt - Data States

```kotlin
// This is a Sealed Class that expresses all possible states of any data operation
sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()   // success + the data
    data class Error(val exception: Throwable) : DataState<Nothing>()  // error
    data object Loading : DataState<Nothing>()              // loading
    data object Idle : DataState<Nothing>()                 // nothing has happened
}

// Usage:
// the login operation returns Flow<DataState<BaseResponse<AuthData>>>
// meaning it can return: Loading → Success(data) or Loading → Error(exception)
```

#### BaseResponse.kt - The Response from the Server

```kotlin
// every API response from the server comes in this shape:
// { "key": 1, "msg": "success", "data": { ... } }
@Serializable
data class BaseResponse<T>(
    @SerialName("key") val key: Int = 0,      // 1 = success, 0 = failure
    @SerialName("msg") val msg: String = "",   // message from the server
    @SerialName("data") val data: T? = null    // the data (Generic)
)
```

#### LoginUseCase.kt - The Login Logic

```kotlin
// the Use Case does two things:
// 1. validates the data (Validation)
// 2. calls the Repository if the data is valid
class LoginUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(phone: String, password: String, ...): Flow<DataState<...>> {
        // 1. Validation
        if (!CommonValidation.isValidPhone(phone))
            throw InValidPhoneException()  // wrong number
        if (!CommonValidation.isValidPassword(password))
            throw InValidPasswordException()  // wrong password

        // 2. API Call
        return homeRepository.login(...)
    }
}
```

#### Repository Interfaces - The Contracts

```kotlin
// This is an Interface - meaning a "contract" that says "I need these functions"
// Who implements it? → the Data Module
interface HomeRepository {
    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ): Flow<DataState<BaseResponse<AuthData>>>
}

// Why an Interface and not a Class?
// Because the Domain doesn't want to know "how" the data is fetched
// (from an API, a Database, or a Cache)
// it only wants to know "what" data is available
```

### Platform-Specific Files:

```
domain/src/androidMain/
└── PlatformLanguage.android.kt   ← Locale.getDefault().language

domain/src/iosMain/
└── PlatformLanguage.ios.kt       ← NSLocale.currentLocale.languageCode
```

---

## 7. Data Module - The Data Layer

The Data Module implements the interfaces defined in Domain and deals with the API and Storage.

### File Structure:

```
data/src/
├── commonMain/kotlin/com/mahalatk/data/
│   ├── datasource/
│   │   ├── PreferenceDataSource.kt      ← Interface for local storage
│   │   ├── PreferenceDataSourceImpl.kt  ← Implementation (Settings + SecureStorage)
│   │   └── SecureStorage.kt            ← Interface for encrypted storage
│   │
│   ├── remote/
│   │   └── HomeEndPoint.kt             ← API endpoints (login)
│   │
│   ├── repository/
│   │   ├── HomeRepositoryImpl.kt       ← implements HomeRepository
│   │   └── PreferenceRepositoryImpl.kt ← implements PreferenceRepository
│   │
│   ├── util/
│   │   ├── SafeApiCall.kt              ← safe wrapper for calling the API
│   │   ├── NetworkConstants.kt         ← network constants
│   │   ├── PreferenceConstants.kt      ← storage keys
│   │   └── TokenHeaderProvider.kt      ← provides the Token for the HTTP headers
│   │
│   ├── platform/
│   │   ├── AppConfig.kt               ← expect: app settings (URLs, Keys)
│   │   ├── DeviceType.kt              ← expect: device type
│   │   └── HttpClientFactory.kt       ← expect: creating the HttpClient + Common plugins
│   │
│   └── di/
│       └── DataKoinModule.kt          ← Koin modules (common + expect platform)
│
├── androidMain/kotlin/com/mahalatk/data/
│   ├── datasource/
│   │   └── AndroidSecureStorage.kt    ← EncryptedSharedPreferences
│   ├── platform/
│   │   ├── AppConfig.android.kt       ← actual: reads from BuildConfig
│   │   ├── DeviceType.android.kt      ← actual: "android"
│   │   └── HttpClientFactory.android.kt ← actual: OkHttp engine
│   └── di/
│       └── AndroidDataModule.kt       ← actual: platform Koin module
│
└── iosMain/kotlin/com/mahalatk/data/
    ├── datasource/
    │   └── IosSecureStorage.kt        ← Settings (temporarily - should be Keychain)
    ├── platform/
    │   ├── AppConfig.ios.kt           ← actual: hardcoded values
    │   ├── DeviceType.ios.kt          ← actual: "ios"
    │   └── HttpClientFactory.ios.kt   ← actual: Darwin engine
    └── di/
        └── IosDataModule.kt           ← actual: platform Koin module
```

### Explanation of the Important Files:

#### SafeApiCall.kt - The Safe Wrapper

```kotlin
// every API call is wrapped with this function to handle errors uniformly
fun <T> safeApiCall(apiCall: suspend () -> T): Flow<DataState<T>> =
    flow {
        withTimeout(120000L) {              // ← Timeout after two minutes
            val response = apiCall.invoke() // ← executes the API call
            emit(handleSuccess(response))   // ← if it succeeds, emit Success
        }
    }
        .onStart { emit(DataState.Loading) }    // ← first thing, emit Loading
        .catch { emit(handleError(it)) }        // ← if there's an error, emit Error
        .flowOn(Dispatchers.Default)            // ← runs on a background thread

// the order: Loading → Success or Loading → Error
```

#### SecureStorage - Shared Interface

```kotlin
// in commonMain - just an interface
interface SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
    suspend fun setValue(key: String, value: Any?)
}

// in androidMain - the implementation using EncryptedSharedPreferences (encrypted)
class AndroidSecureStorage(context: Context) : SecureStorage {
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(...)
    // the data is stored encrypted with AES256
}

// in iosMain - the implementation using Settings (temporarily - should be Keychain)
class IosSecureStorage : SecureStorage {
    private val settings = Settings()
    // the data is stored in NSUserDefaults (not encrypted)
}
```

#### PreferenceDataSourceImpl - Smart Routing

```kotlin
// routes sensitive data to SecureStorage and the rest to regular Settings
class PreferenceDataSourceImpl(
    private val settings: Settings,          // regular storage
    private val secureStorage: SecureStorage  // encrypted storage
) : PreferenceDataSource {

    override suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        // TOKEN and USER_DATA → go to encrypted storage
        if (key == PreferenceConstants.TOKEN || key == PreferenceConstants.USER_DATA) {
            return secureStorage.getValue(key, default)
        }
        // the rest of the data → regular storage
        return flowOf(settings.getStringOrNull(key) ?: default)
    }
}
```

#### HttpClientFactory - Creating the HTTP Client

```kotlin
// in commonMain - the shared plugins
fun HttpClientConfig<*>.installCommonPlugins(json: Json, baseUrl: String) {
    install(ContentNegotiation) { json(json) }  // ← automatic JSON conversion
    install(Logging) { level = LogLevel.BODY }   // ← print requests/responses
    defaultRequest {
        url("$baseUrl/api/")                     // ← Base URL for all requests
        header("lang", getPlatformLanguage())    // ← device language on every request
    }
}

// in androidMain
actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(OkHttp) {                  // ← OkHttp engine
        engine {
            config {
                connectTimeout(120000, TimeUnit.MILLISECONDS)
                readTimeout(120000, TimeUnit.MILLISECONDS)
            }
        }
        installCommonPlugins(json, baseUrl)       // ← the same shared plugins
    }
}

// in iosMain
actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(Darwin) {                   // ← Darwin engine (iOS)
        engine {
            configureRequest { setTimeoutInterval(120.0) }
        }
        installCommonPlugins(json, baseUrl)        // ← the same shared plugins
    }
}
```

#### TokenHeaderProvider - Managing the Token

```kotlin
// caches the Token in memory so that not every request reads from storage
class TokenHeaderProvider(
    private val preferenceRepository: PreferenceRepository
) : TokenCacheManager {

    @Volatile
    private var cachedToken: String = ""  // ← the token kept in memory

    fun getToken(): String {
        return if (cachedToken.isNotEmpty()) "Bearer $cachedToken" else ""
    }

    override fun refreshTokenCache() {
        // reads the token from storage and saves it in the cache
        scope.launch { cachedToken = preferenceRepository.getToken().first() }
    }
}

// it's added automatically to every HTTP request via an interceptor:
fun HttpClient.installTokenInterceptor(tokenProvider: TokenHeaderProvider) {
    plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (token.isNotEmpty()) {
            request.headers["Authorization"] = token  // ← adds the Bearer token
        }
        execute(request)
    }
}
```

---

## 8. Shared Module - The UI Layer

The Shared Module contains all the app's UI written in Compose Multiplatform.

### File Structure:

```
shared/src/commonMain/kotlin/com/mahalatk/
├── ui/
│   ├── theme/
│   │   ├── Color.kt          ← app colors (Light + Dark mode)
│   │   ├── Type.kt           ← Typography (font sizes and types)
│   │   ├── Theme.kt          ← the main Theme
│   │   └── Dimensions.kt     ← fixed sizes (Spacing, Padding, etc.)
│   │
│   ├── navigation/
│   │   ├── Route.kt          ← definition of all Screens
│   │   ├── AppNavigator.kt   ← manages the back stack
│   │   ├── AppNavigation.kt  ← the main App() (Scaffold + Navigation)
│   │   ├── NavigationContent.kt ← links each Route to its Screen
│   │   ├── BottomNavBar.kt   ← the Bottom Navigation
│   │   ├── ScreenConfig.kt   ← settings for each screen (toolbar, padding)
│   │   └── MainViewModel.kt  ← the main ViewModel
│   │
│   ├── managers/
│   │   ├── LoadingManager.kt  ← manages the global loading state
│   │   ├── MessageManager.kt  ← manages messages (Snackbar)
│   │   └── SessionManager.kt  ← manages the session (Auth failures, etc.)
│   │
│   ├── base/
│   │   ├── BaseViewModel.kt          ← the Base for all ViewModels
│   │   ├── BaseState.kt              ← the base State
│   │   └── SessionAwareViewModel.kt  ← ViewModel that watches the session
│   │
│   └── util/
│       ├── NetworkExtensions.kt ← Extensions for dealing with DataState
│       ├── Constants.kt        ← constants (ARABIC, ENGLISH)
│       ├── StringKeys.kt       ← message keys
│       ├── UIMessage.kt        ← message types
│       └── Logger.kt           ← expect: the log function
│
├── common/component/
│   ├── text/DefaultText.kt        ← the unified text component
│   ├── textfield/DefaultTextField.kt ← the unified input field component
│   ├── toolbar/DefaultToolBar.kt   ← the unified Toolbar component
│   └── noRippleClickable.kt       ← Modifier without the press ripple effect
│
├── features/
│   ├── splash/
│   │   ├── SplashScreen.kt    ← the splash screen (1.5 seconds)
│   │   └── SplashViewModel.kt ← splash screen ViewModel
│   │
│   └── auth/login/
│       ├── LoginScreen.kt     ← the login screen
│       ├── LoginViewModel.kt  ← login ViewModel
│       └── LoginState.kt      ← the Login screen state
│
├── fcm/
│   ├── FcmEventHandler.kt    ← notification event handler
│   ├── NotificationItem.kt   ← the notification model
│   └── NotificationKey.kt    ← notification types
│
├── di/
│   ├── SharedKoinModule.kt   ← registers the shared components (ViewModels, Managers)
│   └── UseCaseModule.kt      ← registers the Use Cases
│
├── Platform.kt               ← expect: the Platform name
└── App.kt                    ← the shared entry point
```

### Explanation of the Important Files:

#### Route.kt - Screen Definitions

```kotlin
// every screen in the app has a Route
sealed interface Route {
    data object Splash : Route                          // the splash screen
    data object Login : Route                           // login
    data object Home : Route                            // home
    data object More : Route                            // more
    data class PickLocation(val latLng: LatLngModel?) : Route  // pick a location
    data class Chat(val roomId: String, val title: String) : Route  // the chat
}
```

#### AppNavigator.kt - Managing Navigation

```kotlin
// manages the back stack (the list of open screens)
class AppNavigator {
    private val _backStack = mutableStateListOf<Route>(Route.Splash)
    val currentRoute: Route get() = _backStack.last()

    fun push(route: Route) {
        _backStack.add(route)
    }      // open a new screen
    fun pop() {
        if (_backStack.size > 1) _backStack.removeLast()
    }  // go back
    fun replaceAll(route: Route) {                          // replace all screens
        _backStack.clear()
        _backStack.add(route)
    }
}
```

#### BaseViewModel.kt - The Base ViewModel

```kotlin
// every ViewModel in the app inherits from here
abstract class BaseViewModel<UiState>(
    initialState: UiState,
    private val loadingManager: LoadingManager,
    private val messageManager: MessageManager
) : ViewModel() {

    // the current state of the screen
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = _state.asStateFlow()

    // update the state
    protected fun updateState(update: UiState.() -> UiState) {
        _state.update { it.update() }
    }

    // execute an operation with automatic Loading + Error management
    protected fun executeNetworkAction(action: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                action()
            } catch (e: Exception) {
                messageManager.showMessage(...)
            }
        }
    }
}
```

#### LoginViewModel.kt - A Complete Example

```kotlin
class LoginViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    sessionManager: SessionManager,
    private val loginUseCase: LoginUseCase,        // Use Case from Domain
    private val preferenceRepository: PreferenceRepository,
    private val tokenCacheManager: TokenCacheManager
) : SessionAwareViewModel<LoginState>(
    LoginState(),
    loadingManager,
    messageManager,
    sessionManager
) {

    fun login() {
        executeNetworkAction {
            loginUseCase(
                phone = state.value.mobile,
                password = state.value.password,
                ...
            ).applyCommonSideEffects(this@LoginViewModel)  // manages Loading/Error automatically
            .collect { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    // save the Token
                    preferenceRepository.setToken(dataState.data.data?.token ?: "")
                    tokenCacheManager.refreshTokenCache()
                    preferenceRepository.setIsLogin(true)
                    // navigate to the home screen
                }
                else -> {}
            }
        }
        }
    }
}
```

#### LoginScreen.kt - The Login Screen

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel, navigator: AppNavigator) {
    val state by viewModel.state.collectAsState()

    Column {
        // mobile number field
        DefaultTextField(
            value = state.mobile,
            onValueChange = { viewModel.updateMobile(it) },
            placeholder = "Phone Number"
        )

        // password field
        DefaultTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = "Password",
            isPassword = true
        )

        // login button
        Button(onClick = { viewModel.login() }) {
            Text("Login")
        }
    }
}
```

---

## 9. App Module - Android Entry Point

The simplest module - just an entry point for the Android app:

```
app/
└── src/main/
    ├── AndroidManifest.xml    ← app and permissions definition
    └── res/                   ← Resources (icons, strings)
```

The `app` module depends only on `shared`, and all the real work is in `shared`.

---

## 10. Dependency Injection with Koin

### What is Dependency Injection (DI)?

Instead of every class calling `new` for its dependencies, we register them in one place and Koin
wires them together:

```kotlin
// ❌ Without DI:
class LoginViewModel {
    val useCase = LoginUseCase(HomeRepositoryImpl(HomeEndPoint(HttpClient())))
    // you have to know the whole chain!
}

// ✅ With DI (Koin):
class LoginViewModel(val useCase: LoginUseCase)  // Koin provides it automatically
```

### Structure of the Koin Modules:

```
┌─────────────────────────────────────────────┐
│              platformDataModule              │
│  (Android: SecureStorage with EncryptedPrefs)│
│  (iOS: SecureStorage with Settings)          │
├─────────────────────────────────────────────┤
│              commonDataModule                │
│  Json, Settings, PreferenceDataSource,       │
│  PreferenceRepository, TokenHeaderProvider,  │
│  HttpClient, HomeEndPoint, HomeRepository    │
├─────────────────────────────────────────────┤
│              useCaseModule                    │
│  LoginUseCase                                │
├─────────────────────────────────────────────┤
│              sharedModule                     │
│  LoadingManager, MessageManager,             │
│  SessionManager, FcmEventHandler,            │
│  MainViewModel, SplashViewModel,             │
│  LoginViewModel                              │
├─────────────────────────────────────────────┤
│       appModule (Android only)               │
│  NotificationHandler                         │
└─────────────────────────────────────────────┘
```

### How Does It Work?

```kotlin
// 1. Registration - in DataKoinModule.kt:
val commonDataModule = module {
    single { Json { ignoreUnknownKeys = true } }           // a single instance
    single<HomeRepository> { HomeRepositoryImpl(get()) }   // get() = Koin fetches HomeEndPoint
    single { HomeEndPoint(get()) }                         // get() = Koin fetches HttpClient
}

// 2. Registering the ViewModels - in SharedKoinModule.kt:
val sharedModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    // each get() → Koin fetches the appropriate dependency automatically
}

// 3. Usage - in the Composable:
@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginViewModel>()  // Koin injects automatically
}
```

### How Is Init Done?

```kotlin
// ── Android (App.kt) ──
class App : Application() {
    override fun onCreate() {
        startKoin {
            androidContext(this@App)          // ← this provides the Android Context
            modules(
                platformDataModule,            // ← Platform-specific (SecureStorage)
                commonDataModule,              // ← Common data (Repositories, API)
                useCaseModule,                 // ← Use Cases
                sharedModule,                  // ← ViewModels + Managers
                appModule                      // ← Android-only (Notifications)
            )
        }
    }
}

// ── iOS (KoinInitHelper.kt) ──
object KoinInitHelper {
    fun doInitKoin() {
        startKoin {
            // no androidContext here!
            modules(
                platformDataModule,            // ← Platform-specific (iOS SecureStorage)
                commonDataModule,              // ← the same common modules
                useCaseModule,
                sharedModule
                // no appModule - that's Android only
            )
        }
    }
}
```

### Types of Registration in Koin:

| Type        | Explanation                                   | Example                             |
|-------------|-----------------------------------------------|-------------------------------------|
| `single`    | one instance across the whole app (Singleton) | `single { Json { ... } }`           |
| `factory`   | a new instance each time                      | `factory { LoginUseCase(get()) }`   |
| `viewModel` | bound to the screen's lifecycle               | `viewModel { LoginViewModel(...) }` |

---

## 11. Navigation System

### How Does Navigation Work?

The app uses a **Custom Navigator** built by hand (not Jetpack Navigation):

```kotlin
// 1. AppNavigator manages a stack of screens:
//    [Splash] → push(Login) → [Splash, Login] → replaceAll(Home) → [Home]

// 2. AppNavigation.kt is the Root Composable:
@Composable
fun App() {
    val navigator = remember { AppNavigator() }

    BaseTheme {
        Scaffold(
            topBar = { DefaultToolBar(...) },       // ← the Toolbar
            bottomBar = { BottomNavBar(...) },      // ← Bottom Navigation
            snackbarHost = { ... }                  // ← error messages
        ) {
            NavigationContent(navigator)             // ← content of the current screen
        }
    }
}

// 3. NavigationContent displays the appropriate screen:
@Composable
fun NavigationContent(navigator: AppNavigator) {
    Crossfade(targetState = navigator.currentRoute) { route ->
        when (route) {
            is Route.Splash -> SplashScreen(navigator = navigator)
            is Route.Login -> LoginScreen(navigator = navigator)
            is Route.Home -> { /* Home Screen */
            }
            is Route.More -> { /* More Screen */
            }
            // ...
        }
    }
}
```

### ScreenConfig - Settings for Each Screen:

```kotlin
// each screen has different settings for the Toolbar and the Bottom Bar
fun getScreenConfig(route: Route): ScreenConfig = when (route) {
    is Route.Splash -> ScreenConfig(
        toolbarState = ToolbarState.Hidden,     // no toolbar
        applyTopPadding = false,
        showBottomBar = false                   // no bottom bar
    )
    is Route.Login -> ScreenConfig(
        toolbarState = ToolbarState.AuthTitleWithBack("Login"),
        showBottomBar = false
    )
    is Route.Home -> ScreenConfig(
        toolbarState = ToolbarState.TitleWithNotification("Home"),
        showBottomBar = true                    // has a bottom bar
    )
}
```

---

## 12. Network Layer

### How Do API Calls Work?

```
the user presses Login
        ↓
LoginViewModel.login()
        ↓
LoginUseCase.invoke(phone, password)
        ↓ (Validation ✓)
HomeRepository.login(...)               ← Interface in Domain
        ↓
HomeRepositoryImpl.login(...)           ← Implementation in Data
        ↓
safeApiCall {                           ← safe wrapper
    homeEndPoint.login(...)             ← Ktor HTTP call
}
        ↓
HttpClient (OkHttp/Darwin)
        ↓ (+ Token Header automatically)
POST https://ninety-sheets.com/api/sign-in
        ↓
Flow<DataState<BaseResponse<AuthData>>>
   ↓ Loading
   ↓ Success(data) or Error(exception)
```

### Ktor HttpClient:

```kotlin
// Ktor is a multiplatform HTTP client
// instead of OkHttp (Android only) or URLSession (iOS only)
// Ktor works on both

// the Engine differs per platform:
// Android → OkHttp (fast and stable on Android)
// iOS → Darwin (built on Apple's URLSession)

// but the API calls themselves are in commonMain:
class HomeEndPoint(private val client: HttpClient) {
    suspend fun login(...): BaseResponse<AuthData> =
        client.submitForm(
            url = "sign-in",                    // ← relative URL (Base URL defined in defaultRequest)
            formParameters = parameters {
                append("phone", phone)
                append("password", password)
                // ...
            }
        )
            .body()                                // ← converts the JSON to a Kotlin object automatically
}
```

### The Installed Plugins:

| Plugin                 | Function                                           |
|------------------------|----------------------------------------------------|
| `ContentNegotiation`   | automatic JSON ↔ Kotlin objects conversion         |
| `Logging`              | prints all requests/responses to the console       |
| `defaultRequest`       | adds Base URL + Language header to every request   |
| `HttpSend interceptor` | adds Authorization (Bearer token) to every request |

---

## 13. State Management

### The Flow from the API to the UI:

```
API Response
    ↓
DataState<T> (Success / Error / Loading / Idle)
    ↓
Flow<DataState<T>>    ← reactive stream
    ↓
ViewModel.state       ← StateFlow<UiState>
    ↓
Composable            ← collectAsState()
    ↓
the UI updates automatically
```

### A Practical Example:

```kotlin
// 1. the ViewModel collects DataState:
fun login() {
    executeNetworkAction {
        loginUseCase(phone, password)
            .applyCommonSideEffects(this)    // ← manages Loading/Error automatically
            .collect { state ->
                when (state) {
                    is DataState.Loading -> { /* LoadingManager works automatically */
                    }
                    is DataState.Success -> { /* save the token + navigate */
                    }
                    is DataState.Error -> { /* MessageManager shows the error */
                    }
                    is DataState.Idle -> { /* nothing */
                    }
                }
            }
    }
}

// 2. the UI watches the state:
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()
    // state.mobile, state.password, etc.
    // whenever the state changes → the UI updates automatically
}
```

### The Managers:

```kotlin
// LoadingManager - manages a global loading state for the whole app
class LoadingManager {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun showLoading() {
        _isLoading.value = true
    }
    fun hideLoading() {
        _isLoading.value = false
    }
}
// in AppNavigation.kt:
// if (isLoading) CircularProgressIndicator()

// MessageManager - displays Snackbar messages
class MessageManager {
    private val _messages = MutableSharedFlow<UIMessage>()
    suspend fun showMessage(message: UIMessage) {
        _messages.emit(message)
    }
}

// SessionManager - manages the session
class SessionManager {
    val authFailure: StateFlow<Boolean>   // Token expired
    val blocked: StateFlow<Boolean>        // the account was blocked
    val fcmUpdate: StateFlow<String?>     // FCM token was updated
}
```

---

## 14. Theme and Design System

### The Colors:

```kotlin
// Color.kt - all colors defined as tokens
object ColorLightTokens {
    val Primary = Color(0xFF4E9FE0)        // blue - the primary color
    val Secondary = Color(0xFF543592)      // purple
    val Error = Color(0xFFBA1A1A)          // red for errors
    val Background = Color(0xFFF5F5F5)     // light background
}

object ColorDarkTokens {
    val Primary = Color(0xFF9ECAFF)        // light blue in Dark Mode
    // ...
}
```

### The Dimensions:

```kotlin
// Dimensions.kt - fixed sizes so the design is consistent
object Spacing {
    val xxxSmall = 2.dp
    val xxSmall = 4.dp
    val xSmall = 6.dp
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val xLarge = 24.dp
    val xxLarge = 32.dp
}

object CornerRadius {
    val small = 8.dp
    val medium = 12.dp
    val large = 16.dp
    val full = 100.dp    // fully circular
}
```

### The Theme:

```kotlin
// Theme.kt
@Composable
fun BaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme(...) else lightColorScheme(...)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

// Usage:
BaseTheme {
    // all composables inside here use the defined colors and fonts
    Text("Hello", color = MaterialTheme.colorScheme.primary)
}
```

---

## 15. Firebase and Push Notifications

### How Do Notifications Work?

```
┌──────────────────────────────────────────────┐
│                   Firebase                    │
│              (Google Cloud)                   │
└──────────────┬──────────────┬────────────────┘
               ↓              ↓
        ┌──────────┐  ┌──────────┐
        │ Android  │  │   iOS    │
        │ FCM SDK  │  │ APNs     │
        └────┬─────┘  └────┬─────┘
             ↓              ↓
   FirebaseMessaging   IosFcmHandler
   Receiver.kt         .kt
             ↓              ↓
        ┌──────────────────────┐
        │   FcmEventHandler    │  ← commonMain (shared)
        │  (parses the         │
        │   notification +     │
        │   updates Session)   │
        └──────────────────────┘
```

### Android:

```kotlin
// FirebaseMessagingReceiver.kt - receives the notifications
class FirebaseMessagingReceiver : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        // 1. sends the data to NotificationHandler to display a notification
        // 2. sends the data to FcmEventHandler to update the app state
    }

    override fun onNewToken(token: String) {
        // saves the new FCM token in the preferences
    }
}
```

### iOS:

```kotlin
// IosFcmHandler.kt - a bridge between Swift and Kotlin
class IosFcmHandler(private val fcmEventHandler: FcmEventHandler) {
    fun onNotificationReceived(data: Map<String, String>) {
        fcmEventHandler.handleNotificationData(data)
    }
}

// in Swift:
// let handler = IosKoinHelper().getFcmHandler()
// handler.onNotificationReceived(data: notificationData)
```

### FcmEventHandler (shared):

```kotlin
// parses the notification and determines its type
class FcmEventHandler(
    private val preferenceRepository: PreferenceRepository,
    private val sessionManager: SessionManager
) {
    fun handleNotificationData(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            NotificationKey.ACCOUNT_BLOCK,
            NotificationKey.ACCOUNT_DELETED -> {
                // performs logout
                sessionManager.notifyBlocked()
            }
            else -> {
                // sends a normal update
                sessionManager.notifyFcmUpdate(data.toString())
            }
        }
    }
}
```

---

## 16. How Does iOS Work?

### The Entry Point:

```swift
// in Xcode - iOSApp.swift
import shared    // ← the framework that Kotlin generates

@main
struct iOSApp: App {
    init() {
        // 1. start Koin (DI)
        KoinInitHelper().doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            // 2. display the Compose UI
            ComposeView()
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // 3. create the ViewController from Kotlin
        MainViewControllerKt.MainViewController()
    }
}
```

### The Flow:

```
Swift App starts
    ↓
KoinInitHelper.doInitKoin()     ← Kotlin (registers all the DI)
    ↓
MainViewController()            ← Kotlin (creates a ComposeUIViewController)
    ↓
App() composable                ← commonMain Compose UI
    ↓
SplashScreen → LoginScreen → ... (exactly the same as Android)
```

### The Difference Between Android and iOS:

| The Part       | Android                             | iOS                              |
|----------------|-------------------------------------|----------------------------------|
| Entry point    | `App : Application()`               | Swift `iOSApp`                   |
| Activity/VC    | `MainActivity : ComponentActivity`  | `ComposeUIViewController`        |
| DI Init        | `startKoin { androidContext(...) }` | `startKoin { /* no context */ }` |
| HTTP Engine    | OkHttp                              | Darwin                           |
| Secure Storage | EncryptedSharedPreferences          | Settings (temporarily)           |
| FCM            | `FirebaseMessagingService`          | Swift APNs + `IosFcmHandler`     |
| Notifications  | `NotificationHandler`               | Swift `UNUserNotificationCenter` |
| The UI         | **exactly the same code**           | **exactly the same code**        |

---

## 17. Build Files

### settings.gradle.kts:

```kotlin
// defines the modules in the project
rootProject.name = "Mahalatk"
include(":app")      // Android app
include(":data")     // Data layer (KMP)
include(":domain")   // Domain layer (KMP)
include(":shared")   // UI layer (KMP + CMP)
```

### libs.versions.toml:

```toml
# central file for managing all the versions
# instead of writing the version in every module

[versions]
kotlin = "2.2.21"
ktor = "3.1.1"
koin = "4.1.0"
composeMultiplatform = "1.8.1"

[libraries]
ktor-client-core = { group = "io.ktor", name = "ktor-client-core", version.ref = "ktor" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

### data/build.gradle.kts (KMP Module):

```kotlin
plugins {
    alias(libs.plugins.kotlin.multiplatform)   // ← KMP plugin
    alias(libs.plugins.android.library)        // ← Android library
    alias(libs.plugins.kotlin.serialization)   // ← JSON serialization
}

kotlin {
    // Targets:
    androidTarget { ... }                      // ← Android
    iosX64()                                   // ← iOS Simulator (Intel)
    iosArm64()                                 // ← iOS Device
    iosSimulatorArm64()                        // ← iOS Simulator (Apple Silicon)

    sourceSets {
        commonMain.dependencies { ... }        // ← shared libraries
        androidMain.dependencies { ... }       // ← Android libraries
        iosMain.dependencies { ... }           // ← iOS libraries
    }
}

android {
    namespace = "com.mahalatk.data"
    compileSdk = 36
    // BuildConfig for reading the Config file
}
```

### Config file:

```properties
# these values are read at Build time and converted into BuildConfig on Android
API_KEY="AIzaSyAoZ356P2Ke2Xm_njlJIiYjrgp3NgEkVnI"
REMOTE_URL="https://ninety-sheets.com"
SOCKET_PORT="4777"
```

---

## 18. Complete Flow: From Pressing Login to the Response

```
1. the user types the mobile and password and presses Login
   ↓
2. LoginScreen.kt → viewModel.login()
   ↓
3. LoginViewModel.login() → executeNetworkAction { ... }
   ↓
4. LoginUseCase.invoke(phone, password)
   ↓
5. CommonValidation.isValidPhone(phone)     ← phone >= 9 characters?
   CommonValidation.isValidPassword(password) ← password >= 8 characters?
   ↓ (if it fails → throw ValidationException → MessageManager shows an error)
   ↓ (if it succeeds ↓)
6. HomeRepository.login(countryCode, phone, password, deviceId, socialId)
   ↓ (Interface in Domain)
7. HomeRepositoryImpl.login(...) → safeApiCall { homeEndPoint.login(...) }
   ↓
8. safeApiCall:
   emit(DataState.Loading)     ← LoadingManager.showLoading()
   ↓
9. HomeEndPoint.login(...):
   client.submitForm(url = "sign-in", formParameters = {...})
   ↓
10. HttpClient:
    URL: https://ninety-sheets.com/api/sign-in
    Headers:
      - lang: "ar" (or "en")
      - Authorization: "Bearer eyJ..." (if present)
      - Content-Type: application/x-www-form-urlencoded
    Body:
      - country_code=+966
      - phone=555555555
      - password=12345678
      - device_id=abc123
      - device_type=android (or ios)
   ↓
11. the server responds:
    { "key": 1, "msg": "success", "data": { "id": 1, "name": "...", "token": "eyJ..." } }
   ↓
12. Ktor converts the JSON to BaseResponse<AuthData> (automatically via ContentNegotiation)
   ↓
13. safeApiCall:
    emit(DataState.Success(response))     ← LoadingManager.hideLoading()
   ↓
14. LoginViewModel:
    - preferenceRepository.setToken(token)      ← saves the token (encrypted)
    - tokenCacheManager.refreshTokenCache()     ← refreshes the cache
    - preferenceRepository.setUserData(json)    ← saves the user data
    - preferenceRepository.setIsLogin(true)     ← saves the login state
    - navigator.replaceAll(Route.Home)          ← navigates to the home screen
   ↓
15. NavigationContent displays HomeScreen instead of LoginScreen
```

### If an Error Occurs:

```
Step 10 → the server responds 400:
    { "key": 0, "msg": "Invalid credentials" }
    ↓
convertErrorBody():
    status 400 → CustomException("Invalid credentials")
    ↓
safeApiCall:
    emit(DataState.Error(CustomException))
    ↓
applyCommonSideEffects():
    LoadingManager.hideLoading()
    MessageManager.showMessage("Invalid credentials")
    ↓
AppNavigation.kt:
    a Snackbar appears with the message "Invalid credentials"
```

---

## 19. Libraries Used

| Library                    | Function                                       | Platform            |
|----------------------------|------------------------------------------------|---------------------|
| **Kotlin Multiplatform**   | sharing code between Android and iOS           | all                 |
| **Compose Multiplatform**  | shared UI                                      | all                 |
| **Ktor Client**            | HTTP client                                    | all (OkHttp/Darwin) |
| **Koin**                   | Dependency Injection                           | all                 |
| **kotlinx.serialization**  | JSON ↔ Kotlin conversion                       | all                 |
| **kotlinx.coroutines**     | Async operations (Flows, suspend)              | all                 |
| **Multiplatform Settings** | key-value storage (SharedPrefs/NSUserDefaults) | all                 |
| **Coil 3**                 | image loading                                  | all                 |
| **Compottie**              | Lottie animations                              | all                 |
| **Firebase Messaging**     | Push Notifications                             | Android             |
| **Firebase Crashlytics**   | crash reports                                  | Android             |
| **EncryptedSharedPrefs**   | encrypted storage                              | Android             |
| **ExoPlayer (Media3)**     | video playback                                 | Android             |
| **Google Maps**            | maps                                           | Android             |
| **Socket.IO**              | Real-time communication                        | Android             |

---

## 20. Important Tips and Concepts

### 1. Flow and StateFlow:

```kotlin
// Flow = a stream of data (like a water pipe)
// StateFlow = a Flow that retains the last value (like a reactive variable)

// Flow: you use it for one-off operations (API call)
fun login(): Flow<DataState<AuthData>>  // ← emits Loading → Success/Error and that's it

// StateFlow: you use it for continuous state (UI State)
val state: StateFlow<LoginState>  // ← always retains the current value
```

### 2. Sealed Class/Interface:

```kotlin
// guarantees that all possible states are known at compile time
sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}

// when you do a when → the compiler forces you to handle every state:
when (state) {
    is DataState.Success -> { /* ... */
    }
    is DataState.Error -> { /* ... */
    }
    is DataState.Loading -> { /* ... */
    }
    is DataState.Idle -> { /* ... */
    }
    // if you forget a state → compile error!
}
```

### 3. Coroutines and suspend:

```kotlin
// suspend = a function that may take time (API call, Database read)
// it doesn't block the main thread - it runs in the background

suspend fun login(): BaseResponse<AuthData>  // ← may take time

// viewModelScope = a scope tied to the ViewModel's lifetime
// when the ViewModel is destroyed → all coroutines are cancelled automatically
viewModelScope.launch {
    loginUseCase(phone, password).collect { state ->
        // here we receive every value from the Flow
    }
}
```

### 4. Composable and State:

```kotlin
// @Composable = a function that describes UI
// it doesn't draw directly - it describes "what you want" and Compose draws it

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // collectAsState() = converts a StateFlow into Compose State
    val state by viewModel.state.collectAsState()
    // whenever state changes → this function is called again (recomposition)

    // remember = retains the value across recompositions
    val navigator = remember { AppNavigator() }

    Column {
        Text(state.mobile)  // ← if mobile changes → Text updates automatically
    }
}
```

### 5. The Difference Between Interface and expect/actual:

```kotlin
// Interface: when you want different implementations with the same API
// you register them in Koin and choose which gets injected
interface SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
}
// Android: class AndroidSecureStorage : SecureStorage
// iOS: class IosSecureStorage : SecureStorage

// expect/actual: when you want the compiler to guarantee each platform has an implementation
// no runtime selection - it's decided at compile time
expect val platformDeviceType: String
actual val platformDeviceType: String = "android"  // Android
actual val platformDeviceType: String = "ios"      // iOS
```

### 6. Project Structure Rules:

```
✅ Domain knows nothing about Data or UI
✅ Data implements interfaces from Domain
✅ Shared (UI) uses Use Cases from Domain
✅ commonMain has no Android or iOS APIs
✅ Platform-specific code lives only in androidMain or iosMain
✅ expect/actual for APIs that differ between platforms
✅ Koin wires everything together at runtime
```

---

## Final Notes

### Things That Could Be Improved in the Future:

1. **iOS SecureStorage**: replace `Settings` with `Keychain` for encrypted storage
2. **iOS Config**: read the config from `Info.plist` instead of hardcoded values
3. **Error Handling**: add a retry mechanism and offline support
4. **Testing**: add unit tests in `commonTest`
5. **CI/CD**: set up GitHub Actions for automated builds

### Learning Resources:

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
- [Ktor Client Docs](https://ktor.io/docs/client-create-new-application.html)
- [Koin Docs](https://insert-koin.io/docs/reference/koin-mp/kmp/)
