package com.mohsin.auth.core.di

import co.touchlab.skie.configuration.annotations.DefaultArgumentInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.mohsin.auth.FileStore
import com.mohsin.auth.domain.account.AccountManager
import com.mohsin.auth.domain.account.AccountRepository
import com.mohsin.auth.domain.storage.AccountJsonStorage
import com.mohsin.auth.feature.AccountsViewModel
import com.mohsin.auth.feature.AddViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import com.mohsin.auth.domain.storage.AccountStorage


@DefaultArgumentInterop.Enabled
@FunctionInterop.FileScopeConversion.Enabled
fun initKoin(
    appDeclaration: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclaration()
        modules(
            listOf(
                coreModule
            )
        )
    }


@FunctionInterop.FileScopeConversion.Enabled
fun initKoin(baseUrl: String) = initKoin() {}



val coreModule = module {
    single<CoroutineDispatcher> {
        Dispatchers.IO
    }

    single<FileStore> {
        FileStore("accounts.json")
    }

    single<AccountStorage> {
        AccountJsonStorage(get())
    }

    single<AccountRepository>{
        AccountRepository(storage = get())
    }

    single<AccountManager>{
        AccountManager(repository = get())
    }

    single { AccountsViewModel(get(), get()) }


    single { AddViewModel(get(), get()) }
}