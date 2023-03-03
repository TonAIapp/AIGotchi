package com.digwex

import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.util.Log.DEBUG
import com.digwex.components.log.Log
import com.digwex.utils.SharedPreferencesUtils

object Config {
  const val PREFERENCES_KEY_ACTIVATION_ACCESS_TOKEN = "activation.token"
  const val  PREFERENCES_KEY_ACTIVATION_PIN = "activation.pin"
  const val PREFERENCES_KEY_DEVICE_ID = "activation.device_id"

  private const val PREFERENCES_KEY_ACTIVATION_ORIENTATION = "activation.orientation"
  private const val PREFERENCES_KEY_SETTINGS_WINDOWS_X = "settings.window.x"
  private const val PREFERENCES_KEY_SETTINGS_WINDOWS_Y = "settings.window.y"
  private const val PREFERENCES_KEY_SETTINGS_WINDOWS_W = "settings.window.w"
  private const val PREFERENCES_KEY_SETTINGS_WINDOWS_H = "settings.window.h"
  private const val PREFERENCES_KEY_ROTATION = "settings.rotation"
  private const val PREFERENCES_KEY_EXPANDING_TIMEOUT = "settings.expanding_timeout"
  private const val PREFERENCES_KEY_SETTINGS_LAST_SYNC = "settings.last_sync"
  private const val PREFERENCES_KEY_LASTUNIX = "settings.lastUnix"
  private const val PREFERENCES_KEY_LASTOFFSET = "settings.lastOffset"
  private const val PREFERENCES_KEY_INTERNAL_STORAGE = "settings.internal_storage"
  private const val PREFERENCES_KEY_EXTERNAL_STORAGE = "settings.external_storage"

  private const val PREFERENCES_KEY_STATS_TRAFFIC_LASTRXBYTES = "stats.traffic.lastRxBytes"
  private const val PREFERENCES_KEY_STATS_TRAFFIC_LASTTXBYTES = "stats.traffic.lastTxBytes"
  private const val PREFERENCES_KEY_STATS_TRAFFIC_RXBYTESFORDAY = "stats.traffic.rxBytesForDay"
  private const val PREFERENCES_KEY_STATS_TRAFFIC_TXBYTESFORDAY = "stats.traffic.txBytesForDay"
  private const val PREFERENCES_KEY_STATS_TRAFFIC_NEXTCLEARTIME = "stats.traffic.nextClearTime"

  private const val PREFERENCES_KEY_UPDATE_VERSION_CODE = "update.version.code"
  private const val PREFERENCES_KEY_UPDATE_PATH = "update.path"

  private var mX: Int = 0
  private var mY: Int = 0
  private var mWidth: Int = -1
  private var mHeight: Int = -1
  private var mOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
  private var mRotation: Int = -1
  private var mExpanding: Int = 10
  private var mLastSyncId: Int = 0
  private var mAccessToken: String = ""
  private var mPlayerId: Int = -1
  private var mPin: String = ""
  private var mExternalStorage: String = ""
  private var mInternalStorage: String = ""
  private var mLastRxBytes: Long = Long.MAX_VALUE
  private var mCurrRxBytes: Long = 0
  private var mLastTxBytes: Long = Long.MAX_VALUE
  private var mCurrTxBytes: Long = 0
  private var mNextClearTime: Long = 0
  private var mContentPath: String = ""

  private var mApp: MainApplication = MainApplication.instance
  private var mPreferences: SharedPreferences

  init {
    mPreferences = mApp.mPreferences
    mAccessToken = SharedPreferencesUtils.getString(mPreferences,
      PREFERENCES_KEY_ACTIVATION_ACCESS_TOKEN, mAccessToken)
    mPlayerId = SharedPreferencesUtils.getInt(mPreferences,
      PREFERENCES_KEY_DEVICE_ID, mPlayerId)
    mPin = SharedPreferencesUtils.getString(mPreferences, PREFERENCES_KEY_ACTIVATION_PIN, mPin)

    mInternalStorage = SharedPreferencesUtils.getString(mPreferences,
      PREFERENCES_KEY_INTERNAL_STORAGE, mInternalStorage)
    mExternalStorage = SharedPreferencesUtils.getString(mPreferences,
      PREFERENCES_KEY_EXTERNAL_STORAGE, mExternalStorage)

    mContentPath = "$mExternalStorage/${BuildConfig.CONTENT_DIR}"


    // STATS TRAFFIC
    mLastRxBytes = SharedPreferencesUtils.getLong(mPreferences,
      PREFERENCES_KEY_STATS_TRAFFIC_LASTRXBYTES, mLastRxBytes)
    mCurrRxBytes = SharedPreferencesUtils.getLong(mPreferences,
      PREFERENCES_KEY_STATS_TRAFFIC_RXBYTESFORDAY, mCurrRxBytes)
    mLastTxBytes = SharedPreferencesUtils.getLong(mPreferences,
      PREFERENCES_KEY_STATS_TRAFFIC_LASTRXBYTES, mLastTxBytes)
    mCurrTxBytes = SharedPreferencesUtils.getLong(mPreferences,
      PREFERENCES_KEY_STATS_TRAFFIC_TXBYTESFORDAY, mCurrTxBytes)
    mNextClearTime = SharedPreferencesUtils.getLong(mPreferences,
      PREFERENCES_KEY_STATS_TRAFFIC_NEXTCLEARTIME,
      System.currentTimeMillis() - 1)

    mLastSyncId = SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_SETTINGS_LAST_SYNC,
      mLastSyncId)
    mX = SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_SETTINGS_WINDOWS_X, mX)
    mY = SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_SETTINGS_WINDOWS_Y, mY)
    mWidth = SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_SETTINGS_WINDOWS_W, mWidth)
    mHeight =
      SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_SETTINGS_WINDOWS_H, mHeight)

    mOrientation =
      SharedPreferencesUtils.getInt(mPreferences,
        PREFERENCES_KEY_ACTIVATION_ORIENTATION, mOrientation)

    Log.println(DEBUG, Config::class.java, "%s: %d",
      PREFERENCES_KEY_ACTIVATION_ORIENTATION, mOrientation)

    mRotation = SharedPreferencesUtils.getInt(mPreferences, PREFERENCES_KEY_ROTATION,
      mRotation)

    if (mRotation == -1)
      mRotation = mOrientation

    Log.println(DEBUG, Config::class.java, "%s: %d | x = %d y = %d  w = %d h = %d",
      PREFERENCES_KEY_ROTATION, mRotation, mX, mY, mWidth, mHeight)

    mExpanding = SharedPreferencesUtils.getInt(mPreferences,
      PREFERENCES_KEY_EXPANDING_TIMEOUT, 10)
  }

  fun save() {
    mPreferences.edit()
      .putInt(PREFERENCES_KEY_SETTINGS_WINDOWS_X, mX)
      .putInt(PREFERENCES_KEY_SETTINGS_WINDOWS_Y, mY)
      .putInt(PREFERENCES_KEY_SETTINGS_WINDOWS_W, mWidth)
      .putInt(PREFERENCES_KEY_SETTINGS_WINDOWS_H, mHeight)
      .putInt(PREFERENCES_KEY_ROTATION, mRotation)
      .putInt(PREFERENCES_KEY_EXPANDING_TIMEOUT, mExpanding)
      .apply()
  }

  val itialized: Boolean
    get() = mAccessToken != "" && mPlayerId != -1

  var x: Int
    get() = mX
    set(value) {
      mX = value
    }

  var y: Int
    get() = mY
    set(value) {
      mY = value
    }

  var width: Int
    get() = mWidth
    set(value) {
      mWidth = value
    }

  var height: Int
    get() = mHeight
    set(value) {
      mHeight = value
    }

  var orientation: Int
    get() = mOrientation
    set(value) {
      mOrientation = value
    }

  var rotation: Int
    get() = mRotation
    set(value) {
      mRotation = value
    }

  var expanding: Int
    get() = mExpanding
    set(value) {
      mExpanding = value
    }

  var lastSyncId: Int
    get() = mLastSyncId
    set(value) {
      SharedPreferencesUtils.putInt(mPreferences, PREFERENCES_KEY_SETTINGS_LAST_SYNC, value)
      mLastSyncId = value
    }

  var playerId: Int
    get() = mPlayerId
    set(value) {
      mPlayerId = value
    }

  var accessToken: String
    get() = mAccessToken
    set(value){
     mAccessToken = value
    }

  var pin: String
    get() = mPin
    set(value){
      mPin = value
    }

  var externalStorage: String
    get() = mExternalStorage
    set(value) {
      mExternalStorage = value
      mPreferences.edit().putString(PREFERENCES_KEY_EXTERNAL_STORAGE, value)
        .apply()
    }

  var internalStorage: String
    get() = mInternalStorage
    set(value) {
      mInternalStorage = value
      mPreferences.edit().putString(PREFERENCES_KEY_INTERNAL_STORAGE, value)
        .apply()
    }

  var updateVersionCode: Int
    get() = mPreferences.getInt(PREFERENCES_KEY_UPDATE_VERSION_CODE, -1)
    set(value) = mPreferences.edit().putInt(PREFERENCES_KEY_UPDATE_VERSION_CODE, value).apply()

  var updatePath: String
    get() = mPreferences.getString(PREFERENCES_KEY_UPDATE_PATH, "") ?: ""
    set(value) = mPreferences.edit().putString(PREFERENCES_KEY_UPDATE_PATH, value).apply()

  var lastUnix: Long
    get() = mPreferences.getLong(PREFERENCES_KEY_LASTUNIX, System.currentTimeMillis())
    set(value) = mPreferences.edit().putLong(PREFERENCES_KEY_LASTUNIX, value).apply()

  var lastOffset: Int
    get() = mPreferences.getInt(PREFERENCES_KEY_LASTOFFSET, 0)
    set(value) = mPreferences.edit().putInt(PREFERENCES_KEY_LASTOFFSET, value).apply()

  var lastRxBytes: Long
    get() = mLastRxBytes
    set(value) {
      mLastRxBytes = value
      SharedPreferencesUtils.putLong(mPreferences,
        PREFERENCES_KEY_STATS_TRAFFIC_LASTRXBYTES, value)
    }

  var lastTxBytes: Long
    get() = mLastTxBytes
    set(value) {
      mLastTxBytes = value
      SharedPreferencesUtils.putLong(mPreferences,
        PREFERENCES_KEY_STATS_TRAFFIC_LASTTXBYTES, value)
    }

  var rxBytesForDay: Long
    get() = mCurrRxBytes
    set(value) {
      mCurrRxBytes = value
      SharedPreferencesUtils.putLong(mPreferences,
        PREFERENCES_KEY_STATS_TRAFFIC_RXBYTESFORDAY, value)
    }

  var txBytesForDay: Long
    get() = mCurrTxBytes
    set(value) {
      mCurrTxBytes = value
      SharedPreferencesUtils.putLong(mPreferences,
        PREFERENCES_KEY_STATS_TRAFFIC_RXBYTESFORDAY, value)
    }

  var nextClearTime: Long
    get() = mNextClearTime
    set(value) {
      mNextClearTime = value
      SharedPreferencesUtils.putLong(mPreferences,
        PREFERENCES_KEY_STATS_TRAFFIC_NEXTCLEARTIME, value)
    }

  var contentPath: String
    get() = mContentPath
    set(value) {
      mContentPath = value
    }
}