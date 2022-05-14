package uz.texnopos.smartmanager.settings

import android.content.Context
import android.content.SharedPreferences

class Settings(context: Context) {
    companion object {
        private const val TOKEN = "token"
        private const val SIGNED_IN = "signedIn"
        private const val ROLE = "role"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    var token: String
        set(value) = preferences.edit().putString(TOKEN, value).apply()
        get() = preferences.getString(TOKEN, "") ?: ""

    var signedIn: Boolean
        set(value) = preferences.edit().putBoolean(SIGNED_IN, value).apply()
        get() = preferences.getBoolean(SIGNED_IN, false)

    var role: String
        set(value) = preferences.edit().putString(ROLE, value).apply()
        get() = preferences.getString(ROLE, "") ?: ""
}
