package com.mohsin.auth.feature

import com.mohsin.auth.domain.account.Account
import com.mohsin.auth.domain.account.AccountRepository
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

class AccountsViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val repository: AccountRepository,
) : ViewModel(), KoinComponent {

    val accounts: StateFlow<List<Account>> = repository.accounts

    private val _updateResult = MutableSharedFlow<Account>()
    val updateResult: SharedFlow<Account> = _updateResult

    suspend fun observeAccounts(callback: (List<Account>) -> Unit) {
        accounts.collect(callback)
    }

    fun edit(acc: Account) {
        viewModelScope.launch(dispatcher) {
            repository.edit(acc)
            _updateResult.emit(acc)  // emit event
        }
    }

    fun delete(acc: Account) {
        viewModelScope.launch(dispatcher) {
            repository.delete(acc)
        }
    }
}
