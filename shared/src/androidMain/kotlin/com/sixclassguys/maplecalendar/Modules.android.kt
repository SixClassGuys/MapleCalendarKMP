package com.sixclassguys.maplecalendar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.sixclassguys.maplecalendar.util.AuthManager
import org.koin.dsl.module

val androidPermissionModule = module {

    // Android 전용 PermissionChecker 등록 (Context 주입)
    single { PermissionChecker(get()) }

    single<AuthManager> { AndroidAuthManager(get()) }

    single<MusicPlayer> { AndroidMusicPlayer(get(), "com.sixclassguys.maplecalendar.service.MusicService") }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = {
                // get()을 통해 androidContext()로 등록된 context를 가져옵니다.
                get<Context>().preferencesDataStoreFile("maple_calendar_prefs")
            }
        )
    }
}