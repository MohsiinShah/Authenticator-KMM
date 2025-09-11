package com.mohsin.auth.domain.account

import kotlinx.coroutines.flow.MutableStateFlow
import com.mohsin.auth.domain.storage.AccountStorage

class AccountRepository(
    private val storage: AccountStorage
) {

    val accounts = MutableStateFlow(getAll())

    fun getAll(): List<Account> = storage.getAll()

    fun get(label: String): Account? {
        return storage.get(label)
    }

    suspend fun add(account: Account) {
        storage.add(account)
        emitUpdate()
    }

    suspend fun addAll(accounts: List<Account>) {
        storage.addAll(accounts)
        emitUpdate()
    }

    suspend fun edit(account: Account) {
        storage.edit(account)
        emitUpdate()
    }

    suspend fun delete(account: Account) {
        storage.delete(account)
        emitUpdate()
    }

    private suspend fun emitUpdate() {
        accounts.emit(storage.getAll())
    }

}