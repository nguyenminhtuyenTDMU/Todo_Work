package com.nguyenminhtuyen.todowork.sui.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

fun getEncryptedPrefs(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
        "secure_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

fun saveSeedPhrase(context: Context, mnemonic: List<String>) {
    val prefs = getEncryptedPrefs(context)
    prefs.edit().putString("seed_phrase", mnemonic.joinToString(" ")).apply()
}

fun loadSeedPhrase(context: Context): List<String>? {
    val prefs = getEncryptedPrefs(context)
    val mnemonicStr = prefs.getString("seed_phrase", null) ?: return null
    return mnemonicStr.split(" ")
}

fun hasWallet(context: Context): Boolean {
    val prefs = getEncryptedPrefs(context)
    return prefs.contains("seed_phrase")
}

fun deleteWallet(context: Context) {
    val prefs = getEncryptedPrefs(context)
    prefs.edit().clear().apply()
}