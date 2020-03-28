package kz.iitu.iituboardandroid.di

import kz.iitu.iituboardandroid.api.IituServiceManager
import kz.iitu.iituboardandroid.api.LocalDataSource
import kz.iitu.iituboardandroid.api.NetworkManager
import kz.iitu.iituboardandroid.api.RemoteDataSource
import kz.iitu.iituboardandroid.ui.auth.AuthVM
import kz.iitu.iituboardandroid.ui.login.LoginRepository
import kz.iitu.iituboardandroid.ui.login.LoginRepositoryImpl
import kz.iitu.iituboardandroid.ui.login.LoginVM
import kz.iitu.iituboardandroid.ui.restore.RestorePasswordVM
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val apiModel = module {
    val activeServiceManager = IituServiceManager()
    single { activeServiceManager.createApi() }

    single { NetworkManager(androidContext().applicationContext) }
    single { RemoteDataSource(get(), get()) }
    single { LocalDataSource() }
}

val authModule = module {
    viewModel { AuthVM(get()) }

    single { LoginRepositoryImpl(get(), get()) as LoginRepository }
    viewModel { LoginVM(get()) }

    viewModel { RestorePasswordVM(get()) }
}