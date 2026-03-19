package com.mahalatk.domain.util

object Constants {

    const val TAG = "AppDebug"

    const val NETWORK_TIMEOUT = 300000L
    const val BEARER = "Bearer "
    const val AUTHORIZATION = "Authorization"
    const val LANGUAGE = "lang"

    val language: String
        get() = getPlatformLanguage()
}

object NetworkParams {
    const val METHOD = "_method"
    const val PATCH = "patch"
    const val PUT = "put"

    const val COUNT_NOTIFY = "count-notifications"
    const val COUNTRY_CODE = "country_code"
    const val COUNTRY_CODE_VALUE = "00966"
    const val PHONE = "phone"
    const val PHONE_OR_EMAIL = "phone_or_email"
    const val PASSWORD = "password"
    const val OLD_PASSWORD = "old_password"
    const val CONFIRM_PASSWORD = "password_confirmation"
    const val DEVICE_ID = "device_id"
    const val DEVICE_TYPE = "device_type"
    const val TYPE_ANDROID = "android"
    const val EMAIL = "email"
    const val IS_NOTIFY = "is_notify"
    const val MSG = "message"
    const val NAME = "name"
    const val PLACE_ID = "place_id"
    const val AVATAR = "avatar"
    const val CODE = "code"
    const val LAT = "lat"
    const val LNG = "lng"
    const val ADDRESS = "address"
    const val PAGE = "page"
    const val GENDER = "gender"
    const val ACCOUNT_NAME = "account_name"
    const val BIRTH_DATE = "birthdate"
    const val CITY_ID = "city_id"
    const val COUNTRY_ID = "country_id"
    const val IMAGE = "image"
    const val MAP_DESC = "map_desc"
    const val MAP_DESC_AR = "map_desc[ar]"
    const val MAP_DESC_EN = "map_desc[en]"
    const val NOTES = "notes"
    const val PRICE = "price"
    const val ID = "id"
    const val CITY_AR = "city[ar]"
    const val CITY_EN = "city[en]"
    const val TYPE = "type"
    const val TITLE = "title"
    const val BODY = "body"
    const val MESSAGE = "message"
    const val FROM_DATE = "from_date"
    const val TO_DATE = "to_date"
    const val PAY_TYPE = "pay_type"
    const val PROVIDER_ID = "provider_id"
    const val RATE = "rate"
    const val RATED_ID = "rated_id"
    const val PLACE_AR = "place[ar]"
    const val PLACE_EN = "place[en]"
    const val CITY_LOCATION_AR = "city_location[ar]"
    const val CITY_LOCATION_EN = "city_location[en]"
    const val USER_TYPE = "user_type"
    const val DATE = "date"
    const val BOOK_DATE = "book_date"
    const val ORDER_ID = "order_id"
    const val WORD = "word"
    const val SEARCH = "search"
    const val VIDEO_URL = "video_link"
    const val FACEBOOK = "facebook"
    const val TWITTER = "twitter"
    const val WHATSAPP = "whatsapp"
    const val PACKAGE_ID = "package_id"
    const val TIKTOK = "tiktok"
    const val SNAPCHAT = "snapchat"
    const val INSTAGRAM = "instagram"
    const val YOUTUBE = "youtube"
    const val TELEGRAM = "telegram"
    const val DELIVERY_SERVICE = "delivery_service"
    const val DELIVERY_PRICE = "delivery_price"
    const val NAME_AR = "name[ar]"
    const val NAME_EN = "name[en]"
    const val DESC_AR = "description[ar]"
    const val DESC_EN = "description[en]"
    const val IS_DISCOUNT = "is_discount"
    const val DISCOUNT = "discount"
    const val QUANTITY = "quantity"
    const val PRODUCTS = "products"
    const val PRODUCT_ID = "product_id"
    const val NOTIFICATION_ID = "notification_id"
}


object MethodType {
    const val PATCH = "patch"
    const val PUT = "put"
}

object LoginType {
    const val LOGIN_WITH_EMAIL = "login_with_email"
    const val LOGIN_WITH_PHONE = "login_with_phone"
}

object ResponseStatus {
    const val SUCCESS = "success"
    const val NEED_ACTIVATE = "needActive"
    const val FAILED = "fail"
    const val ACTIVE = "active"
    const val NOT_ACTIVE = "waitingApproval"
    const val BLOCK = "blocked"
    const val PENDING = "pending"
    const val UN_AUTH = "unauthenticated"
    const val EXCEPTION = "exception"
}

object FailRequestCode {
    const val FAIL = 400
    const val UN_AUTH = 419
    const val BLOCKED = 423
    const val EXCEPTION = 500
}
