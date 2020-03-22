package kz.iitu.iituboardandroid.di

import kz.iitu.iituboardandroid.api.IituServiceManager
import kz.iitu.iituboardandroid.api.NetworkManager
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.ui.auth.AuthVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModel = module {
    val activeServiceManager = IituServiceManager()
    single { activeServiceManager.createApi() }

    single { NetworkManager(androidContext().applicationContext) }
    single { RemoteDataSource(get()) }
}

val authModule = module {
    viewModel { AuthVM(get()) }
}