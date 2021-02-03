package com.elluzion.cephytools.etc

import android.content.Context
import android.content.SharedPreferences

object SharedPrefController {
    // May be used later idk
    fun setSharedPrefString(context: Context, keyName: String, value: String) {
        val pref: SharedPreferences = context.getSharedPreferences("Preferences", 0)
        val editor = pref.edit()
        editor.putString(keyName, value)
        editor.apply()
    }
    fun setSharedPrefBool(context: Context, keyName: String, value: Boolean) {
        val pref: SharedPreferences = context.getSharedPreferences("Preferences", 0)
        val editor = pref.edit()
        editor.putBoolean(keyName, value)
        editor.apply()
    }
    fun getSharedPrefString(context: Context, keyName: String, defValue: String): String? {
        val pref: SharedPreferences = context.getSharedPreferences("Preferences", 0)
        return pref.getString(keyName, defValue)
    }
    fun getSharedPrefInt(context: Context, keyName: String, defValue: Int): Int {
        val pref: SharedPreferences = context.getSharedPreferences("Preferences", 0)
        return pref.getInt(keyName, defValue)
    }
    fun getSharedPrefBool(context: Context, keyName: String, defValue: Boolean): Boolean {
        val pref: SharedPreferences = context.getSharedPreferences("Preferences", 0)
        return pref.getBoolean(keyName, defValue)
    }
}