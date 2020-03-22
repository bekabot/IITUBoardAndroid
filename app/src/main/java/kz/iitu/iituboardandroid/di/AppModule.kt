package kz.iitu.iituboardandroid.di

import kz.iitu.iituboardandroid.ui.auth.AuthVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    viewModel { AuthVM() }
}