package com.pactsafe.pactsafeandroidsdk.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.pactsafe.pactsafeandroidsdk.models.PSGroup
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ApplicationPreferencesImp(private val context: Context) : ApplicationPreferences {

    companion object {
        private const val PREFERENCES = "com.pactsafe.PREFERENCES"
        private const val GROUP = "group"
        private const val SITE_ACCESS_ID = "site_access_id"
        private const val GROUP_KEY = "group_key"
        private const val TAG = "ApplicationPref"
    }

    private val preferences
        get() = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    override var group: PSGroup?
        get() {
            return try {
                val stringValue = preferences.getString(GROUP, null) ?: return null
                return Gson().fromJson(stringValue, PSGroup::class.java)
            } catch (e: Exception) {
                Log.e(TAG, "There was an error fetching this item.", e)
                null
            }
        }
        set(value) {
            val stringValue = Gson().toJson(value)
            preferences.edit().putString(GROUP, stringValue).apply()
        }
    override var siteAccessId: String?
        get() = preferences.getString(SITE_ACCESS_ID, "") ?: ""
        set(value) {
            preferences.edit().putString(SITE_ACCESS_ID, value).apply()
        }
    override var psGroupKey: String
        get() = preferences.getString(GROUP_KEY, "") ?: ""
        set(value) {
            preferences.edit().putString(GROUP_KEY, value).apply()
        }

    override fun clear() {
        preferences.edit().clear().apply()
    }

}