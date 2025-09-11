package com.mohsin.auth.domain.storage

import com.mohsin.auth.FileStore
import kotlinx.serialization.json.Json
import com.mohsin.auth.domain.account.Account

private typealias AccountCache = LinkedHashMap<String, Account>

class AccountJsonStorage(
    private val fileStore: FileStore
) : AccountStorage {

    private var cache: AccountCache? = null

    override fun getAll(): List<Account> =
        getOrParseData().values.toList()

    override fun get(label: String): Account? =
        getOrParseData()[label.lowercase()]

    override fun add(account: Account) {
        editFile { cache ->
            if (!cache.contains(account.key()))
                cache[account.key()] = account
        }
    }

    override fun addAll(accounts: List<Account>) {
        editFile { cache ->
            accounts.forEach { acc ->
                if (!cache.contains(acc.key()))
                    cache[acc.key()] = acc
            }
        }
    }

    override fun edit(account: Account) = add(account)

    override fun delete(account: Account) {
        editFile { cache -> cache.remove(account.key()) }
    }

    override fun export(): String =
        Json { prettyPrint = true }.encodeToString(getOrParseData())

    private fun editFile(edit: (AccountCache) -> Unit) {
        val parsed = getOrParseData()
        edit(parsed)
        fileStore.writeText(Json.encodeToString(parsed))
    }

    private fun getOrParseData(): AccountCache {
        val cache = this.cache
        return if (cache == null) {
            val parsed: AccountCache = if (fileStore.exists()) {
                Json.decodeFromString(fileStore.readText())
            } else {
                linkedMapOf()
            }
            this.cache = parsed
            parsed
        } else cache
    }

    private fun Account.key() = label.lowercase()
}
