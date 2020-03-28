package kz.iitu.iituboardandroid

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import kz.iitu.iituboardandroid.di.apiModel
import kz.iitu.iituboardandroid.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(
                    apiModel, authModule
                )
            )
        }

        Timber.plant(Timber.DebugTree())

        wiki.depasquale.mcache.Cache.with(this)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}