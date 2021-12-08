package com.farshidsj.woocertestapp.core.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import com.farshidsj.woocertestapp.feature_login.domain.model.AuthenticationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferences(
    private val context: Context
) {

    private val dataStore: DataStore<Preferences> by lazy {
        return@lazy context.createDataStore(
            name = Constants.APP_PREFERENCES
        )
    }

    companion object {
        val CONSUMER_KEY = stringPreferencesKey(name = Constants.CONSUMER_KEY)
        val CONSUMER_SECRET = stringPreferencesKey(name = Constants.CONSUMER_SECRET)
        val NAME = stringPreferencesKey(name = Constants.NAME)
        val EMAIL = stringPreferencesKey(name = Constants.EMAIL)
    }

    suspend fun saveAuthForm(authenticationModel: AuthenticationModel) {
        dataStore.edit {
            it[NAME] = authenticationModel.name
            it[EMAIL] = authenticationModel.email
            it[CONSUMER_KEY] = authenticationModel.consumerKey
            it[CONSUMER_SECRET] = authenticationModel.consumerSecret
        }
    }

    fun getAuthForm() = dataStore.data.map {
        AuthenticationModel(
            name = it[NAME] ?: "",
            email = it[EMAIL] ?: "",
            consumerKey = it[CONSUMER_KEY] ?: "",
            consumerSecret = it[CONSUMER_SECRET] ?: ""
        )
    }

    suspend fun saveConsumerKey(string: String) {
        dataStore.edit {
            it[CONSUMER_KEY] = string
        }
    }

    val getConsumerKey: Flow<String> = dataStore.data.map { it[CONSUMER_KEY] ?: "" }

    suspend fun saveConsumerSecret(string: String) {
        dataStore.edit {
            it[CONSUMER_SECRET] = string
        }
    }

    val getConsumerSecret: Flow<String> = dataStore.data.map { it[CONSUMER_SECRET] ?: "" }

    suspend fun saveName(string: String) {
        dataStore.edit {
            it[NAME] = string
        }
    }

    val getName: Flow<String> = dataStore.data.map { it[NAME] ?: "" }

    suspend fun saveEmail(string: String) {
        dataStore.edit {
            it[EMAIL] = string
        }
    }

    val getEmail: Flow<String> = dataStore.data.map { it[EMAIL] ?: "" }

}