# Mahalatak - دليل المشروع الكامل

## Compose Multiplatform (CMP) + Clean Architecture

---

## الفهرس

1. [مقدمة - إيه هو KMP و CMP؟](#1-مقدمة)
2. [هيكل المشروع العام](#2-هيكل-المشروع-العام)
3. [الموديولات الأربعة](#3-الموديولات-الأربعة)
4. [مفهوم expect/actual](#4-مفهوم-expectactual)
5. [Source Sets - إيه هي؟](#5-source-sets)
6. [Domain Module - طبقة البيزنس](#6-domain-module)
7. [Data Module - طبقة البيانات](#7-data-module)
8. [Shared Module - طبقة الـ UI](#8-shared-module)
9. [App Module - نقطة دخول Android](#9-app-module)
10. [Dependency Injection مع Koin](#10-dependency-injection-مع-koin)
11. [الـ Navigation System](#11-navigation-system)
12. [الـ Network Layer (Ktor)](#12-network-layer)
13. [إدارة الحالة (State Management)](#13-state-management)
14. [الـ Theme و Design System](#14-theme-و-design-system)
15. [Firebase و Push Notifications](#15-firebase-و-push-notifications)
16. [إزاي iOS بيشتغل؟](#16-إزاي-ios-بيشتغل)
17. [ملفات الـ Build و الـ Configuration](#17-ملفات-الـ-build)
18. [Flow كامل: من الضغط على Login لحد الاستجابة](#18-flow-كامل)
19. [المكتبات المستخدمة](#19-المكتبات-المستخدمة)
20. [نصائح ومفاهيم مهمة](#20-نصائح-ومفاهيم-مهمة)

---

## 1. مقدمة

### إيه هو KMP (Kotlin Multiplatform)؟

KMP هي تقنية من JetBrains بتسمحلك تكتب كود Kotlin **مرة واحدة** ويشتغل على أكتر من platform (
Android + iOS).

**الفكرة ببساطة:**

- الكود المشترك (Business Logic, Network, Models) → بتكتبه مرة واحدة في `commonMain`
- الكود الخاص بكل platform (Camera, GPS, Encryption) → بتكتبه في `androidMain` أو `iosMain`

### إيه هو CMP (Compose Multiplatform)؟

CMP هي امتداد لـ Jetpack Compose عشان تشتغل على iOS كمان. يعني بدل ما تكتب UI بـ Compose لـ Android
و SwiftUI لـ iOS، بتكتب Compose **مرة واحدة** والاتنين بيشاركوا نفس الـ UI.

### ليه نستخدمهم؟

```
بدون KMP:                         مع KMP:
┌──────────┐ ┌──────────┐         ┌──────────────────┐
│ Android  │ │   iOS    │         │   commonMain     │
│ (Kotlin) │ │ (Swift)  │         │  (كود مشترك)     │
│          │ │          │         ├────────┬─────────┤
│ UI       │ │ UI       │         │Android │  iOS    │
│ Network  │ │ Network  │         │ Main   │  Main   │
│ Logic    │ │ Logic    │         │(خاص)   │ (خاص)  │
│ Models   │ │ Models   │         └────────┴─────────┘
└──────────┘ └──────────┘
  كود × 2                           كود × 1
```

---

## 2. هيكل المشروع العام

```
Mahalatak/
├── app/                    ← نقطة دخول Android (Application + Activity)
├── data/                   ← طبقة البيانات (API + Storage) - KMP Module
│   └── src/
│       ├── commonMain/     ← كود مشترك (Ktor endpoints, Repositories, Utils)
│       ├── androidMain/    ← كود Android بس (OkHttp, EncryptedSharedPreferences)
│       └── iosMain/        ← كود iOS بس (Darwin engine, Settings)
├── domain/                 ← طبقة البيزنس (Entities, Use Cases, Interfaces) - KMP Module
│   └── src/
│       ├── commonMain/     ← كود مشترك (Models, Interfaces, Validation)
│       ├── androidMain/    ← كود Android بس (Locale)
│       └── iosMain/        ← كود iOS بس (NSLocale)
├── shared/                 ← طبقة الـ UI (Compose Multiplatform) - KMP Module
│   └── src/
│       ├── commonMain/     ← كود مشترك (Screens, ViewModels, Theme, Navigation)
│       ├── androidMain/    ← كود Android بس (Firebase, Notifications, Activity)
│       └── iosMain/        ← كود iOS بس (MainViewController, KoinInit)
├── gradle/
│   └── libs.versions.toml  ← ملف إدارة الـ dependencies وإصداراتها
├── build.gradle.kts         ← Root build file
├── settings.gradle.kts      ← تعريف الموديولات
└── Config                   ← API keys و URLs
```

---

## 3. الموديولات الأربعة

المشروع مبني على **Clean Architecture** ومقسم لـ 4 modules:

```
┌─────────────────────────────────────────────┐
│                    app                       │
│         (Android Entry Point)                │
│              يعتمد على: shared               │
├─────────────────────────────────────────────┤
│                  shared                      │
│    (UI Layer - Compose Multiplatform)        │
│         يعتمد على: domain + data             │
├──────────────────────┬──────────────────────┤
│        data          │       domain          │
│   (Data Layer)       │   (Business Layer)    │
│  يعتمد على: domain   │   مستقل - لا يعتمد   │
│                      │   على أي module تاني  │
└──────────────────────┴──────────────────────┘
```

### القاعدة الذهبية:

- **domain** مش بيعتمد على حد ← مستقل تمامًا
- **data** بيعتمد على domain ← عشان ينفذ الـ interfaces
- **shared** بيعتمد على domain + data ← عشان يستخدم الكل
- **app** بيعتمد على shared ← نقطة دخول Android بس

### ليه الترتيب ده؟

عشان لو غيرت طريقة تخزين البيانات (مثلاً من API لـ Firebase) → تغير data بس.
لو غيرت الـ UI (مثلاً redesign) → تغير shared بس.
الـ domain ثابت لأنه البيزنس logic.

---

## 4. مفهوم expect/actual

ده من أهم المفاهيم في KMP. لما يكون عندك كود **مشترك** بس محتاج implementation **مختلف** لكل
platform:

### إزاي بيشتغل؟

```kotlin
// ──── commonMain (التعريف - "أنا محتاج الحاجة دي") ────
expect fun getPlatformLanguage(): String

// ──── androidMain (التنفيذ لـ Android) ────
actual fun getPlatformLanguage(): String {
    return java.util.Locale.getDefault().language  // بيستخدم Java API
}

// ──── iosMain (التنفيذ لـ iOS) ────
actual fun getPlatformLanguage(): String {
    return NSLocale.currentLocale.languageCode ?: "en"  // بيستخدم iOS API
}
```

### أمثلة من المشروع:

| expect (commonMain)                     | Android actual        | iOS actual               |
|-----------------------------------------|-----------------------|--------------------------|
| `expect val platformDeviceType: String` | `"android"`           | `"ios"`                  |
| `expect object AppConfig`               | يقرأ من `BuildConfig` | قيم hardcoded            |
| `expect fun createPlatformHttpClient()` | `HttpClient(OkHttp)`  | `HttpClient(Darwin)`     |
| `expect fun getPlatformLanguage()`      | `Locale.getDefault()` | `NSLocale.currentLocale` |

### متى تستخدم expect/actual؟

- لما تحتاج API خاص بالـ platform (Locale, Keychain, Context)
- لما تحتاج library مختلفة لكل platform (OkHttp vs Darwin)
- لما تحتاج config مختلف (BuildConfig vs hardcoded)

---

## 5. Source Sets - إيه هي؟

كل KMP module عنده **source sets** - يعني مجموعات كود منفصلة:

```
data/src/
├── commonMain/    ← الكود اللي بيشتغل على كل الـ platforms
│                    (Kotlin Pure + Multiplatform Libraries)
│
├── androidMain/   ← الكود اللي بيشتغل على Android بس
│                    (Android APIs + Java Libraries)
│
├── iosMain/       ← الكود اللي بيشتغل على iOS بس
│                    (iOS APIs + Objective-C/Swift Interop)
│
└── commonTest/    ← تيستات مشتركة
```

### القواعد:

1. `commonMain` **مش** بيقدر يستخدم أي Android أو iOS APIs
2. `androidMain` بيقدر يستخدم كل حاجة في `commonMain` + Android APIs
3. `iosMain` بيقدر يستخدم كل حاجة في `commonMain` + iOS APIs
4. `androidMain` و `iosMain` **مش** بيشوفوا بعض

```
         commonMain
        /          \
androidMain      iosMain
(يشوف common)   (يشوف common)
(مش بيشوف iOS)  (مش بيشوف Android)
```

---

## 6. Domain Module - طبقة البيزنس

الـ domain هو **قلب التطبيق** - فيه البيزنس logic والقواعد. مش بيعتمد على أي framework أو library
خارجية (ماعدا Coroutines و Serialization).

### هيكل الملفات:

```
domain/src/commonMain/kotlin/com/mahalatk/domain/
├── entity/                    ← الـ Data Models
│   ├── AuthData.kt            ← بيانات المستخدم بعد Login
│   ├── LatLngModel.kt         ← بيانات الموقع
│   └── base/
│       ├── BaseResponse.kt    ← الـ Response wrapper من السيرفر
│       └── AnyResponse.kt     ← Response فاضي
│
├── repository/                ← الـ Interfaces (العقود)
│   ├── HomeRepository.kt      ← عقد عمليات الـ Home (login)
│   └── PreferenceRepository.kt ← عقد إدارة الـ Preferences
│
├── usecase/auth/              ← الـ Use Cases (البيزنس Logic)
│   └── LoginUseCase.kt        ← منطق الـ Login (validation + call)
│
├── exception/                 ← الـ Exceptions الخاصة
│   └── ValidationException.kt ← أخطاء التحقق (Phone, Password)
│
└── util/                      ← أدوات مساعدة
    ├── Constants.kt           ← ثوابت (أسماء الـ API params, أكواد الأخطاء)
    ├── DataState.kt           ← حالات البيانات (Success, Error, Loading, Idle)
    ├── NetworkExceptions.kt   ← أخطاء الشبكة (Timeout, Auth, Server, etc.)
    ├── CommonValidation.kt    ← التحقق من الـ Phone والـ Password
    ├── Gson.kt                ← دوال تحويل JSON (toJson, fromJson)
    ├── TokenCacheManager.kt   ← Interface لإدارة cache الـ Token
    ├── PlatformLanguage.kt    ← expect function للغة الجهاز
    └── ResponseStatusConstants.kt ← ثوابت حالة الـ Response
```

### شرح الملفات المهمة:

#### DataState.kt - حالات البيانات

```kotlin
// ده Sealed Class بيعبر عن كل الحالات الممكنة لأي عملية بيانات
sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()   // نجاح + البيانات
    data class Error(val exception: Throwable) : DataState<Nothing>()  // خطأ
    data object Loading : DataState<Nothing>()              // تحميل
    data object Idle : DataState<Nothing>()                 // مفيش حاجة حصلت
}

// الاستخدام:
// عملية الـ login بترجع Flow<DataState<BaseResponse<AuthData>>>
// يعني ممكن ترجع: Loading → Success(data) أو Loading → Error(exception)
```

#### BaseResponse.kt - الـ Response من السيرفر

```kotlin
// كل API response من السيرفر بييجي بالشكل ده:
// { "key": 1, "msg": "success", "data": { ... } }
@Serializable
data class BaseResponse<T>(
    @SerialName("key") val key: Int = 0,      // 1 = نجاح, 0 = فشل
    @SerialName("msg") val msg: String = "",   // رسالة من السيرفر
    @SerialName("data") val data: T? = null    // البيانات (Generic)
)
```

#### LoginUseCase.kt - منطق الـ Login

```kotlin
// الـ Use Case بيعمل حاجتين:
// 1. يتحقق من صحة البيانات (Validation)
// 2. يستدعي الـ Repository لو البيانات صحيحة
class LoginUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(phone: String, password: String, ...): Flow<DataState<...>> {
        // 1. Validation
        if (!CommonValidation.isValidPhone(phone))
            throw InValidPhoneException()  // رقم غلط
        if (!CommonValidation.isValidPassword(password))
            throw InValidPasswordException()  // باسورد غلط

        // 2. API Call
        return homeRepository.login(...)
    }
}
```

#### Repository Interfaces - العقود

```kotlin
// ده Interface - يعني "عقد" بيقول "أنا محتاج الوظائف دي"
// مين هينفذها؟ → Data Module
interface HomeRepository {
    suspend fun login(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ): Flow<DataState<BaseResponse<AuthData>>>
}

// ليه Interface مش Class؟
// عشان Domain مش عايز يعرف "إزاي" البيانات بتتجاب
// (من API ولا من Database ولا من Cache)
// هو بس عايز يعرف "إيه" البيانات المتاحة
```

### ملفات Platform-Specific:

```
domain/src/androidMain/
└── PlatformLanguage.android.kt   ← Locale.getDefault().language

domain/src/iosMain/
└── PlatformLanguage.ios.kt       ← NSLocale.currentLocale.languageCode
```

---

## 7. Data Module - طبقة البيانات

الـ Data Module بينفذ الـ interfaces اللي في Domain وبيتعامل مع الـ API والـ Storage.

### هيكل الملفات:

```
data/src/
├── commonMain/kotlin/com/mahalatk/data/
│   ├── datasource/
│   │   ├── PreferenceDataSource.kt      ← Interface للتخزين المحلي
│   │   ├── PreferenceDataSourceImpl.kt  ← Implementation (Settings + SecureStorage)
│   │   └── SecureStorage.kt            ← Interface للتخزين المشفر
│   │
│   ├── remote/
│   │   └── HomeEndPoint.kt             ← API endpoints (login)
│   │
│   ├── repository/
│   │   ├── HomeRepositoryImpl.kt       ← تنفيذ HomeRepository
│   │   └── PreferenceRepositoryImpl.kt ← تنفيذ PreferenceRepository
│   │
│   ├── util/
│   │   ├── SafeApiCall.kt              ← غلاف آمن لاستدعاء الـ API
│   │   ├── NetworkConstants.kt         ← ثوابت الشبكة
│   │   ├── PreferenceConstants.kt      ← مفاتيح التخزين
│   │   └── TokenHeaderProvider.kt      ← يوفر الـ Token للـ HTTP headers
│   │
│   ├── platform/
│   │   ├── AppConfig.kt               ← expect: إعدادات التطبيق (URLs, Keys)
│   │   ├── DeviceType.kt              ← expect: نوع الجهاز
│   │   └── HttpClientFactory.kt       ← expect: إنشاء HttpClient + Common plugins
│   │
│   └── di/
│       └── DataKoinModule.kt          ← Koin modules (common + expect platform)
│
├── androidMain/kotlin/com/mahalatk/data/
│   ├── datasource/
│   │   └── AndroidSecureStorage.kt    ← EncryptedSharedPreferences
│   ├── platform/
│   │   ├── AppConfig.android.kt       ← actual: يقرأ من BuildConfig
│   │   ├── DeviceType.android.kt      ← actual: "android"
│   │   └── HttpClientFactory.android.kt ← actual: OkHttp engine
│   └── di/
│       └── AndroidDataModule.kt       ← actual: platform Koin module
│
└── iosMain/kotlin/com/mahalatk/data/
    ├── datasource/
    │   └── IosSecureStorage.kt        ← Settings (مؤقتاً - المفروض Keychain)
    ├── platform/
    │   ├── AppConfig.ios.kt           ← actual: قيم hardcoded
    │   ├── DeviceType.ios.kt          ← actual: "ios"
    │   └── HttpClientFactory.ios.kt   ← actual: Darwin engine
    └── di/
        └── IosDataModule.kt           ← actual: platform Koin module
```

### شرح الملفات المهمة:

#### SafeApiCall.kt - الغلاف الآمن

```kotlin
// كل API call بيتلف بالدالة دي عشان يتعامل مع الأخطاء بشكل موحد
fun <T> safeApiCall(apiCall: suspend () -> T): Flow<DataState<T>> =
    flow {
        withTimeout(120000L) {              // ← Timeout بعد دقيقتين
            val response = apiCall.invoke() // ← ينفذ الـ API call
            emit(handleSuccess(response))   // ← لو نجح يبعت Success
        }
    }
        .onStart { emit(DataState.Loading) }    // ← أول حاجة يبعت Loading
        .catch { emit(handleError(it)) }        // ← لو فيه error يبعت Error
        .flowOn(Dispatchers.Default)            // ← يشتغل على background thread

// الترتيب: Loading → Success أو Loading → Error
```

#### SecureStorage - Interface مشترك

```kotlin
// في commonMain - مجرد interface
interface SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
    suspend fun setValue(key: String, value: Any?)
}

// في androidMain - التنفيذ باستخدام EncryptedSharedPreferences (مشفر)
class AndroidSecureStorage(context: Context) : SecureStorage {
    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(...)
    // البيانات بتتخزن مشفرة بـ AES256
}

// في iosMain - التنفيذ باستخدام Settings (مؤقتاً - المفروض Keychain)
class IosSecureStorage : SecureStorage {
    private val settings = Settings()
    // البيانات بتتخزن في NSUserDefaults (مش مشفرة)
}
```

#### PreferenceDataSourceImpl - التوجيه الذكي

```kotlin
// بيوجه البيانات الحساسة لـ SecureStorage والباقي لـ Settings العادي
class PreferenceDataSourceImpl(
    private val settings: Settings,          // تخزين عادي
    private val secureStorage: SecureStorage  // تخزين مشفر
) : PreferenceDataSource {

    override suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        // TOKEN و USER_DATA → يروحوا للتخزين المشفر
        if (key == PreferenceConstants.TOKEN || key == PreferenceConstants.USER_DATA) {
            return secureStorage.getValue(key, default)
        }
        // باقي البيانات → تخزين عادي
        return flowOf(settings.getStringOrNull(key) ?: default)
    }
}
```

#### HttpClientFactory - إنشاء الـ HTTP Client

```kotlin
// في commonMain - الـ plugins المشتركة
fun HttpClientConfig<*>.installCommonPlugins(json: Json, baseUrl: String) {
    install(ContentNegotiation) { json(json) }  // ← تحويل JSON تلقائي
    install(Logging) { level = LogLevel.BODY }   // ← طباعة الـ requests/responses
    defaultRequest {
        url("$baseUrl/api/")                     // ← Base URL لكل الـ requests
        header("lang", getPlatformLanguage())    // ← لغة الجهاز في كل request
    }
}

// في androidMain
actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(OkHttp) {                  // ← محرك OkHttp
        engine {
            config {
                connectTimeout(120000, TimeUnit.MILLISECONDS)
                readTimeout(120000, TimeUnit.MILLISECONDS)
            }
        }
        installCommonPlugins(json, baseUrl)       // ← نفس الـ plugins المشتركة
    }
}

// في iosMain
actual fun createPlatformHttpClient(json: Json, baseUrl: String): HttpClient {
    return HttpClient(Darwin) {                   // ← محرك Darwin (iOS)
        engine {
            configureRequest { setTimeoutInterval(120.0) }
        }
        installCommonPlugins(json, baseUrl)        // ← نفس الـ plugins المشتركة
    }
}
```

#### TokenHeaderProvider - إدارة الـ Token

```kotlin
// بيخزن الـ Token في الذاكرة (cache) عشان مش كل request يروح يقرأ من الـ storage
class TokenHeaderProvider(
    private val preferenceRepository: PreferenceRepository
) : TokenCacheManager {

    @Volatile
    private var cachedToken: String = ""  // ← الـ token محفوظ في الذاكرة

    fun getToken(): String {
        return if (cachedToken.isNotEmpty()) "Bearer $cachedToken" else ""
    }

    override fun refreshTokenCache() {
        // بيقرأ الـ token من الـ storage ويحفظه في الـ cache
        scope.launch { cachedToken = preferenceRepository.getToken().first() }
    }
}

// بيتضاف تلقائي في كل HTTP request عن طريق interceptor:
fun HttpClient.installTokenInterceptor(tokenProvider: TokenHeaderProvider) {
    plugin(HttpSend).intercept { request ->
        val token = tokenProvider.getToken()
        if (token.isNotEmpty()) {
            request.headers["Authorization"] = token  // ← يضيف Bearer token
        }
        execute(request)
    }
}
```

---

## 8. Shared Module - طبقة الـ UI

الـ Shared Module فيه كل الـ UI بتاع التطبيق مكتوب بـ Compose Multiplatform.

### هيكل الملفات:

```
shared/src/commonMain/kotlin/com/mahalatk/
├── ui/
│   ├── theme/
│   │   ├── Color.kt          ← ألوان التطبيق (Light + Dark mode)
│   │   ├── Type.kt           ← Typography (أحجام وأنواع الخطوط)
│   │   ├── Theme.kt          ← الـ Theme الرئيسي
│   │   └── Dimensions.kt     ← مقاسات ثابتة (Spacing, Padding, etc.)
│   │
│   ├── navigation/
│   │   ├── Route.kt          ← تعريف كل الـ Screens
│   │   ├── AppNavigator.kt   ← إدارة الـ back stack
│   │   ├── AppNavigation.kt  ← الـ App() الرئيسي (Scaffold + Navigation)
│   │   ├── NavigationContent.kt ← ربط كل Route بالـ Screen بتاعته
│   │   ├── BottomNavBar.kt   ← الـ Bottom Navigation
│   │   ├── ScreenConfig.kt   ← إعدادات كل شاشة (toolbar, padding)
│   │   └── MainViewModel.kt  ← الـ ViewModel الرئيسي
│   │
│   ├── managers/
│   │   ├── LoadingManager.kt  ← إدارة حالة التحميل العامة
│   │   ├── MessageManager.kt  ← إدارة الرسائل (Snackbar)
│   │   └── SessionManager.kt  ← إدارة الجلسة (Auth failures, etc.)
│   │
│   ├── base/
│   │   ├── BaseViewModel.kt          ← الـ Base لكل ViewModels
│   │   ├── BaseState.kt              ← الـ State الأساسي
│   │   └── SessionAwareViewModel.kt  ← ViewModel بيراقب الجلسة
│   │
│   └── util/
│       ├── NetworkExtensions.kt ← Extensions للتعامل مع DataState
│       ├── Constants.kt        ← ثوابت (ARABIC, ENGLISH)
│       ├── StringKeys.kt       ← مفاتيح الرسائل
│       ├── UIMessage.kt        ← أنواع الرسائل
│       └── Logger.kt           ← expect: دالة اللوج
│
├── common/component/
│   ├── text/DefaultText.kt        ← مكون النص الموحد
│   ├── textfield/DefaultTextField.kt ← مكون حقل الإدخال الموحد
│   ├── toolbar/DefaultToolBar.kt   ← مكون الـ Toolbar الموحد
│   └── noRippleClickable.kt       ← Modifier بدون تأثير الضغط
│
├── features/
│   ├── splash/
│   │   ├── SplashScreen.kt    ← شاشة البداية (1.5 ثانية)
│   │   └── SplashViewModel.kt ← ViewModel شاشة البداية
│   │
│   └── auth/login/
│       ├── LoginScreen.kt     ← شاشة تسجيل الدخول
│       ├── LoginViewModel.kt  ← ViewModel تسجيل الدخول
│       └── LoginState.kt      ← حالة شاشة الـ Login
│
├── fcm/
│   ├── FcmEventHandler.kt    ← معالج أحداث الإشعارات
│   ├── NotificationItem.kt   ← نموذج الإشعار
│   └── NotificationKey.kt    ← أنواع الإشعارات
│
├── di/
│   ├── SharedKoinModule.kt   ← تسجيل المكونات المشتركة (ViewModels, Managers)
│   └── UseCaseModule.kt      ← تسجيل الـ Use Cases
│
├── Platform.kt               ← expect: اسم الـ Platform
└── App.kt                    ← نقطة الدخول المشتركة
```

### شرح الملفات المهمة:

#### Route.kt - تعريف الشاشات

```kotlin
// كل شاشة في التطبيق ليها Route
sealed interface Route {
    data object Splash : Route                          // شاشة البداية
    data object Login : Route                           // تسجيل الدخول
    data object Home : Route                            // الرئيسية
    data object More : Route                            // المزيد
    data class PickLocation(val latLng: LatLngModel?) : Route  // اختيار موقع
    data class Chat(val roomId: String, val title: String) : Route  // الشات
}
```

#### AppNavigator.kt - إدارة التنقل

```kotlin
// بيدير الـ back stack (قائمة الشاشات المفتوحة)
class AppNavigator {
    private val _backStack = mutableStateListOf<Route>(Route.Splash)
    val currentRoute: Route get() = _backStack.last()

    fun push(route: Route) {
        _backStack.add(route)
    }      // فتح شاشة جديدة
    fun pop() {
        if (_backStack.size > 1) _backStack.removeLast()
    }  // رجوع
    fun replaceAll(route: Route) {                          // استبدال كل الشاشات
        _backStack.clear()
        _backStack.add(route)
    }
}
```

#### BaseViewModel.kt - الـ ViewModel الأساسي

```kotlin
// كل ViewModel في التطبيق بيورث من هنا
abstract class BaseViewModel<UiState>(
    initialState: UiState,
    private val loadingManager: LoadingManager,
    private val messageManager: MessageManager
) : ViewModel() {

    // الحالة الحالية للشاشة
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<UiState> = _state.asStateFlow()

    // تحديث الحالة
    protected fun updateState(update: UiState.() -> UiState) {
        _state.update { it.update() }
    }

    // تنفيذ عملية مع إدارة Loading + Error تلقائياً
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

#### LoginViewModel.kt - مثال كامل

```kotlin
class LoginViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    sessionManager: SessionManager,
    private val loginUseCase: LoginUseCase,        // Use Case من Domain
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
            ).applyCommonSideEffects(this@LoginViewModel)  // يدير Loading/Error تلقائي
            .collect { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    // حفظ Token
                    preferenceRepository.setToken(dataState.data.data?.token ?: "")
                    tokenCacheManager.refreshTokenCache()
                    preferenceRepository.setIsLogin(true)
                    // الانتقال للشاشة الرئيسية
                }
                else -> {}
            }
        }
        }
    }
}
```

#### LoginScreen.kt - شاشة الـ Login

```kotlin
@Composable
fun LoginScreen(viewModel: LoginViewModel, navigator: AppNavigator) {
    val state by viewModel.state.collectAsState()

    Column {
        // حقل رقم الموبايل
        DefaultTextField(
            value = state.mobile,
            onValueChange = { viewModel.updateMobile(it) },
            placeholder = "Phone Number"
        )

        // حقل الباسورد
        DefaultTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            placeholder = "Password",
            isPassword = true
        )

        // زر تسجيل الدخول
        Button(onClick = { viewModel.login() }) {
            Text("Login")
        }
    }
}
```

---

## 9. App Module - نقطة دخول Android

أبسط module - مجرد نقطة دخول لتطبيق Android:

```
app/
└── src/main/
    ├── AndroidManifest.xml    ← تعريف التطبيق والصلاحيات
    └── res/                   ← Resources (icons, strings)
```

الـ `app` module بيعتمد على `shared` بس، وكل الشغل الفعلي في `shared`.

---

## 10. Dependency Injection مع Koin

### إيه هو Dependency Injection (DI)؟

بدل ما كل class يعمل `new` للـ dependencies بتاعته، بنسجلهم في مكان واحد و Koin بيوصلهم ببعض:

```kotlin
// ❌ بدون DI:
class LoginViewModel {
    val useCase = LoginUseCase(HomeRepositoryImpl(HomeEndPoint(HttpClient())))
    // لازم تعرف كل الـ chain!
}

// ✅ مع DI (Koin):
class LoginViewModel(val useCase: LoginUseCase)  // Koin بيوفره تلقائي
```

### هيكل الـ Koin Modules:

```
┌─────────────────────────────────────────────┐
│              platformDataModule              │
│  (Android: SecureStorage مع EncryptedPrefs)  │
│  (iOS: SecureStorage مع Settings)            │
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
│       appModule (Android فقط)                │
│  NotificationHandler                         │
└─────────────────────────────────────────────┘
```

### إزاي بيشتغل؟

```kotlin
// 1. تسجيل (Registration) - في DataKoinModule.kt:
val commonDataModule = module {
    single { Json { ignoreUnknownKeys = true } }           // instance واحد
    single<HomeRepository> { HomeRepositoryImpl(get()) }   // get() = Koin يجيب HomeEndPoint
    single { HomeEndPoint(get()) }                         // get() = Koin يجيب HttpClient
}

// 2. تسجيل الـ ViewModels - في SharedKoinModule.kt:
val sharedModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get(), get(), get()) }
    // كل get() → Koin بيجيب الـ dependency المناسبة تلقائي
}

// 3. الاستخدام - في الـ Composable:
@Composable
fun LoginScreen() {
    val viewModel = koinViewModel<LoginViewModel>()  // Koin بيعمل inject تلقائي
}
```

### إزاي بيتعمل Init؟

```kotlin
// ── Android (App.kt) ──
class App : Application() {
    override fun onCreate() {
        startKoin {
            androidContext(this@App)          // ← ده بيوفر Android Context
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
            // مفيش androidContext هنا!
            modules(
                platformDataModule,            // ← Platform-specific (iOS SecureStorage)
                commonDataModule,              // ← نفس الـ common modules
                useCaseModule,
                sharedModule
                // مفيش appModule - ده Android بس
            )
        }
    }
}
```

### أنواع التسجيل في Koin:

| النوع       | الشرح                                   | مثال                                |
|-------------|-----------------------------------------|-------------------------------------|
| `single`    | instance واحد في كل التطبيق (Singleton) | `single { Json { ... } }`           |
| `factory`   | instance جديد كل مرة                    | `factory { LoginUseCase(get()) }`   |
| `viewModel` | مربوط بـ lifecycle الشاشة               | `viewModel { LoginViewModel(...) }` |

---

## 11. Navigation System

### إزاي الـ Navigation بيشتغل؟

التطبيق بيستخدم **Custom Navigator** مبني يدوي (مش Jetpack Navigation):

```kotlin
// 1. AppNavigator بيدير stack من الشاشات:
//    [Splash] → push(Login) → [Splash, Login] → replaceAll(Home) → [Home]

// 2. AppNavigation.kt هو الـ Root Composable:
@Composable
fun App() {
    val navigator = remember { AppNavigator() }

    BaseTheme {
        Scaffold(
            topBar = { DefaultToolBar(...) },       // ← الـ Toolbar
            bottomBar = { BottomNavBar(...) },      // ← Bottom Navigation
            snackbarHost = { ... }                  // ← رسائل الخطأ
        ) {
            NavigationContent(navigator)             // ← محتوى الشاشة الحالية
        }
    }
}

// 3. NavigationContent بيعرض الشاشة المناسبة:
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

### ScreenConfig - إعدادات كل شاشة:

```kotlin
// كل شاشة ليها إعدادات مختلفة للـ Toolbar والـ Bottom Bar
fun getScreenConfig(route: Route): ScreenConfig = when (route) {
    is Route.Splash -> ScreenConfig(
        toolbarState = ToolbarState.Hidden,     // مفيش toolbar
        applyTopPadding = false,
        showBottomBar = false                   // مفيش bottom bar
    )
    is Route.Login -> ScreenConfig(
        toolbarState = ToolbarState.AuthTitleWithBack("Login"),
        showBottomBar = false
    )
    is Route.Home -> ScreenConfig(
        toolbarState = ToolbarState.TitleWithNotification("Home"),
        showBottomBar = true                    // فيه bottom bar
    )
}
```

---

## 12. Network Layer

### إزاي الـ API Calls بتشتغل؟

```
المستخدم يضغط Login
        ↓
LoginViewModel.login()
        ↓
LoginUseCase.invoke(phone, password)
        ↓ (Validation ✓)
HomeRepository.login(...)               ← Interface في Domain
        ↓
HomeRepositoryImpl.login(...)           ← Implementation في Data
        ↓
safeApiCall {                           ← غلاف آمن
    homeEndPoint.login(...)             ← Ktor HTTP call
}
        ↓
HttpClient (OkHttp/Darwin)
        ↓ (+ Token Header تلقائي)
POST https://basephp.aait-sa.com/api/sign-in
        ↓
Flow<DataState<BaseResponse<AuthData>>>
   ↓ Loading
   ↓ Success(data) أو Error(exception)
```

### Ktor HttpClient:

```kotlin
// Ktor هو HTTP client متعدد المنصات
// بدل OkHttp (Android فقط) أو URLSession (iOS فقط)
// Ktor بيشتغل على الاتنين

// الـ Engine مختلف لكل platform:
// Android → OkHttp (سريع ومستقر على Android)
// iOS → Darwin (مبني على URLSession بتاع Apple)

// لكن الـ API calls نفسها في commonMain:
class HomeEndPoint(private val client: HttpClient) {
    suspend fun login(...): BaseResponse<AuthData> =
        client.submitForm(
            url = "sign-in",                    // ← relative URL (Base URL متعرف في defaultRequest)
            formParameters = parameters {
                append("phone", phone)
                append("password", password)
                // ...
            }
        ).body()                                // ← يحول الـ JSON لـ Kotlin object تلقائي
}
```

### الـ Plugins المثبتة:

| Plugin                 | الوظيفة                                        |
|------------------------|------------------------------------------------|
| `ContentNegotiation`   | تحويل JSON ↔ Kotlin objects تلقائي             |
| `Logging`              | طباعة كل الـ requests/responses في الـ console |
| `defaultRequest`       | إضافة Base URL + Language header لكل request   |
| `HttpSend interceptor` | إضافة Authorization (Bearer token) لكل request |

---

## 13. State Management

### الـ Flow من الـ API للـ UI:

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
UI يتحدث تلقائي
```

### مثال عملي:

```kotlin
// 1. الـ ViewModel بيجمع DataState:
fun login() {
    executeNetworkAction {
        loginUseCase(phone, password)
            .applyCommonSideEffects(this)    // ← يدير Loading/Error تلقائي
            .collect { state ->
                when (state) {
                    is DataState.Loading -> { /* LoadingManager بيشتغل تلقائي */
                    }
                    is DataState.Success -> { /* حفظ الـ token + navigate */
                    }
                    is DataState.Error -> { /* MessageManager بيعرض الخطأ */
                    }
                    is DataState.Idle -> { /* مفيش حاجة */
                    }
                }
            }
    }
}

// 2. الـ UI بيراقب الحالة:
@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()
    // state.mobile, state.password, etc.
    // كل ما الحالة تتغير → الـ UI يتحدث تلقائي
}
```

### الـ Managers:

```kotlin
// LoadingManager - بيدير Loading عام للتطبيق كله
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
// في AppNavigation.kt:
// if (isLoading) CircularProgressIndicator()

// MessageManager - بيعرض Snackbar messages
class MessageManager {
    private val _messages = MutableSharedFlow<UIMessage>()
    suspend fun showMessage(message: UIMessage) {
        _messages.emit(message)
    }
}

// SessionManager - بيدير الجلسة
class SessionManager {
    val authFailure: StateFlow<Boolean>   // Token انتهى
    val blocked: StateFlow<Boolean>        // الحساب اتحظر
    val fcmUpdate: StateFlow<String?>     // FCM token اتحدث
}
```

---

## 14. Theme و Design System

### الألوان:

```kotlin
// Color.kt - كل الألوان معرفة كـ tokens
object ColorLightTokens {
    val Primary = Color(0xFF4E9FE0)        // أزرق - اللون الرئيسي
    val Secondary = Color(0xFF543592)      // بنفسجي
    val Error = Color(0xFFBA1A1A)          // أحمر للأخطاء
    val Background = Color(0xFFF5F5F5)     // خلفية فاتحة
}

object ColorDarkTokens {
    val Primary = Color(0xFF9ECAFF)        // أزرق فاتح في Dark Mode
    // ...
}
```

### المقاسات:

```kotlin
// Dimensions.kt - مقاسات ثابتة عشان التصميم يكون consistent
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
    val full = 100.dp    // دائري بالكامل
}
```

### الـ Theme:

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

// الاستخدام:
BaseTheme {
    // كل الـ composables جوا هنا بتستخدم الألوان والخطوط المعرفة
    Text("Hello", color = MaterialTheme.colorScheme.primary)
}
```

---

## 15. Firebase و Push Notifications

### إزاي الإشعارات بتشتغل؟

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
        │   FcmEventHandler    │  ← commonMain (مشترك)
        │  (يحلل الإشعار +    │
        │   يحدث الـ Session)  │
        └──────────────────────┘
```

### Android:

```kotlin
// FirebaseMessagingReceiver.kt - بيستقبل الإشعارات
class FirebaseMessagingReceiver : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        // 1. يبعت البيانات لـ NotificationHandler عشان يعرض notification
        // 2. يبعت البيانات لـ FcmEventHandler عشان يحدث الـ app state
    }

    override fun onNewToken(token: String) {
        // يحفظ الـ FCM token الجديد في الـ preferences
    }
}
```

### iOS:

```kotlin
// IosFcmHandler.kt - bridge بين Swift و Kotlin
class IosFcmHandler(private val fcmEventHandler: FcmEventHandler) {
    fun onNotificationReceived(data: Map<String, String>) {
        fcmEventHandler.handleNotificationData(data)
    }
}

// في Swift:
// let handler = IosKoinHelper().getFcmHandler()
// handler.onNotificationReceived(data: notificationData)
```

### FcmEventHandler (مشترك):

```kotlin
// بيحلل الإشعار ويحدد النوع بتاعه
class FcmEventHandler(
    private val preferenceRepository: PreferenceRepository,
    private val sessionManager: SessionManager
) {
    fun handleNotificationData(data: Map<String, String>) {
        val type = data["type"]
        when (type) {
            NotificationKey.ACCOUNT_BLOCK,
            NotificationKey.ACCOUNT_DELETED -> {
                // يعمل logout
                sessionManager.notifyBlocked()
            }
            else -> {
                // يبعت update عادي
                sessionManager.notifyFcmUpdate(data.toString())
            }
        }
    }
}
```

---

## 16. إزاي iOS بيشتغل؟

### نقطة الدخول:

```swift
// في Xcode - iOSApp.swift
import shared    // ← الـ framework اللي Kotlin بيولده

@main
struct iOSApp: App {
    init() {
        // 1. تشغيل Koin (DI)
        KoinInitHelper().doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            // 2. عرض الـ Compose UI
            ComposeView()
        }
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // 3. إنشاء الـ ViewController من Kotlin
        MainViewControllerKt.MainViewController()
    }
}
```

### الـ Flow:

```
Swift App starts
    ↓
KoinInitHelper.doInitKoin()     ← Kotlin (يسجل كل الـ DI)
    ↓
MainViewController()            ← Kotlin (يعمل ComposeUIViewController)
    ↓
App() composable                ← commonMain Compose UI
    ↓
SplashScreen → LoginScreen → ... (نفس Android بالظبط)
```

### الفرق بين Android و iOS:

| الجزء          | Android                             | iOS                              |
|----------------|-------------------------------------|----------------------------------|
| نقطة الدخول    | `App : Application()`               | Swift `iOSApp`                   |
| Activity/VC    | `MainActivity : ComponentActivity`  | `ComposeUIViewController`        |
| DI Init        | `startKoin { androidContext(...) }` | `startKoin { /* no context */ }` |
| HTTP Engine    | OkHttp                              | Darwin                           |
| Secure Storage | EncryptedSharedPreferences          | Settings (مؤقتاً)                |
| FCM            | `FirebaseMessagingService`          | Swift APNs + `IosFcmHandler`     |
| Notifications  | `NotificationHandler`               | Swift `UNUserNotificationCenter` |
| الـ UI         | **نفس الكود بالظبط**                | **نفس الكود بالظبط**             |

---

## 17. ملفات الـ Build

### settings.gradle.kts:

```kotlin
// بيعرف الموديولات الموجودة في المشروع
rootProject.name = "Mahalatk"
include(":app")      // Android app
include(":data")     // Data layer (KMP)
include(":domain")   // Domain layer (KMP)
include(":shared")   // UI layer (KMP + CMP)
```

### libs.versions.toml:

```toml
# ملف مركزي لإدارة كل الـ versions
# بدل ما تكتب الـ version في كل module

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
        commonMain.dependencies { ... }        // ← مكتبات مشتركة
        androidMain.dependencies { ... }       // ← مكتبات Android
        iosMain.dependencies { ... }           // ← مكتبات iOS
    }
}

android {
    namespace = "com.mahalatk.data"
    compileSdk = 36
    // BuildConfig لقراءة الـ Config file
}
```

### Config file:

```properties
# القيم دي بتتقرأ في Build time وبتتحول لـ BuildConfig على Android
API_KEY="AIzaSyAoZ356P2Ke2Xm_njlJIiYjrgp3NgEkVnI"
REMOTE_URL="https://basephp.aait-sa.com"
SOCKET_PORT="4777"
```

---

## 18. Flow كامل: من الضغط على Login لحد الاستجابة

```
1. المستخدم يكتب الموبايل والباسورد ويضغط Login
   ↓
2. LoginScreen.kt → viewModel.login()
   ↓
3. LoginViewModel.login() → executeNetworkAction { ... }
   ↓
4. LoginUseCase.invoke(phone, password)
   ↓
5. CommonValidation.isValidPhone(phone)     ← phone >= 9 حروف؟
   CommonValidation.isValidPassword(password) ← password >= 8 حروف؟
   ↓ (لو فشل → throw ValidationException → MessageManager يعرض خطأ)
   ↓ (لو نجح ↓)
6. HomeRepository.login(countryCode, phone, password, deviceId, socialId)
   ↓ (Interface في Domain)
7. HomeRepositoryImpl.login(...) → safeApiCall { homeEndPoint.login(...) }
   ↓
8. safeApiCall:
   emit(DataState.Loading)     ← LoadingManager.showLoading()
   ↓
9. HomeEndPoint.login(...):
   client.submitForm(url = "sign-in", formParameters = {...})
   ↓
10. HttpClient:
    URL: https://basephp.aait-sa.com/api/sign-in
    Headers:
      - lang: "ar" (أو "en")
      - Authorization: "Bearer eyJ..." (لو موجود)
      - Content-Type: application/x-www-form-urlencoded
    Body:
      - country_code=+966
      - phone=555555555
      - password=12345678
      - device_id=abc123
      - device_type=android (أو ios)
   ↓
11. السيرفر يرد:
    { "key": 1, "msg": "success", "data": { "id": 1, "name": "...", "token": "eyJ..." } }
   ↓
12. Ktor يحول الـ JSON لـ BaseResponse<AuthData> (تلقائي بـ ContentNegotiation)
   ↓
13. safeApiCall:
    emit(DataState.Success(response))     ← LoadingManager.hideLoading()
   ↓
14. LoginViewModel:
    - preferenceRepository.setToken(token)      ← يحفظ الـ token (مشفر)
    - tokenCacheManager.refreshTokenCache()     ← يحدث الـ cache
    - preferenceRepository.setUserData(json)    ← يحفظ بيانات المستخدم
    - preferenceRepository.setIsLogin(true)     ← يحفظ حالة الدخول
    - navigator.replaceAll(Route.Home)          ← ينتقل للشاشة الرئيسية
   ↓
15. NavigationContent يعرض HomeScreen بدل LoginScreen
```

### لو حصل خطأ:

```
Step 10 → السيرفر يرد 400:
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
    Snackbar يظهر بالرسالة "Invalid credentials"
```

---

## 19. المكتبات المستخدمة

| المكتبة                    | الوظيفة                                      | Platform             |
|----------------------------|----------------------------------------------|----------------------|
| **Kotlin Multiplatform**   | مشاركة الكود بين Android و iOS               | الكل                 |
| **Compose Multiplatform**  | UI مشترك                                     | الكل                 |
| **Ktor Client**            | HTTP client                                  | الكل (OkHttp/Darwin) |
| **Koin**                   | Dependency Injection                         | الكل                 |
| **kotlinx.serialization**  | تحويل JSON ↔ Kotlin                          | الكل                 |
| **kotlinx.coroutines**     | عمليات Async (Flows, suspend)                | الكل                 |
| **Multiplatform Settings** | تخزين key-value (SharedPrefs/NSUserDefaults) | الكل                 |
| **Coil 3**                 | تحميل الصور                                  | الكل                 |
| **Compottie**              | Lottie animations                            | الكل                 |
| **Firebase Messaging**     | Push Notifications                           | Android              |
| **Firebase Crashlytics**   | تقارير الأخطاء                               | Android              |
| **EncryptedSharedPrefs**   | تخزين مشفر                                   | Android              |
| **ExoPlayer (Media3)**     | تشغيل الفيديو                                | Android              |
| **Google Maps**            | خرائط                                        | Android              |
| **Socket.IO**              | Real-time communication                      | Android              |

---

## 20. نصائح ومفاهيم مهمة

### 1. Flow و StateFlow:

```kotlin
// Flow = stream من البيانات (زي أنبوب مياه)
// StateFlow = Flow بيحتفظ بآخر قيمة (زي متغير reactive)

// Flow: بتستخدمه لعمليات مؤقتة (API call)
fun login(): Flow<DataState<AuthData>>  // ← بيبعت Loading → Success/Error وخلاص

// StateFlow: بتستخدمه للحالة المستمرة (UI State)
val state: StateFlow<LoginState>  // ← بيحتفظ بالقيمة الحالية دايماً
```

### 2. Sealed Class/Interface:

```kotlin
// بتضمن إن كل الحالات الممكنة معروفة في compile time
sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data object Idle : DataState<Nothing>()
}

// لما تعمل when → الـ compiler يجبرك تتعامل مع كل الحالات:
when (state) {
    is DataState.Success -> { /* ... */
    }
    is DataState.Error -> { /* ... */
    }
    is DataState.Loading -> { /* ... */
    }
    is DataState.Idle -> { /* ... */
    }
    // لو نسيت حالة → compile error!
}
```

### 3. Coroutines و suspend:

```kotlin
// suspend = دالة ممكن تتأخر (API call, Database read)
// مش بتوقف الـ main thread - بتشتغل في الـ background

suspend fun login(): BaseResponse<AuthData>  // ← ممكن تاخد وقت

// viewModelScope = scope مربوط بعمر الـ ViewModel
// لما الـ ViewModel يتدمر → كل الـ coroutines بتتلغي تلقائي
viewModelScope.launch {
    loginUseCase(phone, password).collect { state ->
        // هنا بنستقبل كل القيم من الـ Flow
    }
}
```

### 4. Composable و State:

```kotlin
// @Composable = دالة بتوصف UI
// مش بترسم مباشرة - بتوصف "عايز إيه" و Compose بيرسمه

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    // collectAsState() = بيحول StateFlow لـ Compose State
    val state by viewModel.state.collectAsState()
    // كل ما state تتغير → الدالة دي بتتنادي تاني (recomposition)

    // remember = بيحفظ القيمة بين الـ recompositions
    val navigator = remember { AppNavigator() }

    Column {
        Text(state.mobile)  // ← لو mobile اتغير → Text يتحدث تلقائي
    }
}
```

### 5. الفرق بين Interface و expect/actual:

```kotlin
// Interface: لما عايز implementations مختلفة بنفس الـ API
// بتسجلهم في Koin وبتختار مين يتحقن
interface SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
}
// Android: class AndroidSecureStorage : SecureStorage
// iOS: class IosSecureStorage : SecureStorage

// expect/actual: لما عايز الـ compiler يضمنلك إن كل platform عنده implementation
// مفيش runtime selection - بيتحدد في compile time
expect val platformDeviceType: String
actual val platformDeviceType: String = "android"  // Android
actual val platformDeviceType: String = "ios"      // iOS
```

### 6. قواعد هيكل المشروع:

```
✅ Domain مش بيعرف حاجة عن Data أو UI
✅ Data بينفذ interfaces من Domain
✅ Shared (UI) بيستخدم Use Cases من Domain
✅ commonMain مش فيه أي Android أو iOS APIs
✅ Platform-specific code في androidMain أو iosMain بس
✅ expect/actual للـ APIs المختلفة بين الـ platforms
✅ Koin بيربط كل حاجة ببعض في runtime
```

---

## ملاحظات أخيرة

### حاجات ممكن تتحسن مستقبلاً:

1. **iOS SecureStorage**: استبدال `Settings` بـ `Keychain` للتخزين المشفر
2. **iOS Config**: قراءة الـ config من `Info.plist` بدل hardcoded values
3. **Error Handling**: إضافة retry mechanism وoffline support
4. **Testing**: إضافة unit tests في `commonTest`
5. **CI/CD**: إعداد GitHub Actions للبناء التلقائي

### مصادر للتعلم:

- [Kotlin Multiplatform Docs](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)
- [Ktor Client Docs](https://ktor.io/docs/client-create-new-application.html)
- [Koin Docs](https://insert-koin.io/docs/reference/koin-mp/kmp/)
