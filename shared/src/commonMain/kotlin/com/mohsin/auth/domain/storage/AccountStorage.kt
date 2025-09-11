package com.mohsin.auth.domain.storage

import com.mohsin.auth.domain.account.Account

interface AccountStorage {

    fun getAll(): List<Account>

    fun get(label: String): Account?

    fun add(account: Account)

    fun addAll(accounts: List<Account>)

    fun edit(account: Account)

    fun delete(account: Account)

    fun export(): String

}