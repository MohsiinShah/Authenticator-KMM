package com.mohsin.auth.feature

import co.touchlab.skie.configuration.annotations.FlowInterop
import co.touchlab.skie.configuration.annotations.FunctionInterop
import com.mohsin.auth.domain.Constants
import com.mohsin.auth.domain.account.Account
import com.mohsin.auth.domain.account.AccountCreationException
import com.mohsin.auth.domain.account.AccountManager
import com.mohsin.auth.domain.account.CreationError
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.core.component.KoinComponent

class AddViewModel(
    private val dispatcher: CoroutineDispatcher,
    private val manager: AccountManager
) : ViewModel(), KoinComponent {

    // One-time success event
    private val _success = MutableSharedFlow<Account>()

    @FlowInterop.Enabled
    val success: SharedFlow<Account> = _success

    // One-time error event
    private val _error = MutableSharedFlow<CreationError>()
    val error: SharedFlow<CreationError> = _error

    @FunctionInterop.LegacyName.Disabled
    fun createByUri(uri: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val created = manager.createByUri(uri)
                _success.emit(created)
            } catch (ex: AccountCreationException) {
                _error.emit(ex.kind)
            } catch (th: Throwable) {
                th.printStackTrace()
                _error.emit(CreationError.UNDEFINED)
            }
        }
    }

    fun createTotp(name: String, secret: String, interval: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                val account = manager.createTotpAccount(
                    name,
                    null,
                    secret,
                    Constants.DEFAULT_ALGORITHM,
                    Constants.DEFAULT_DIGITS,
                    interval
                )
                _success.emit(account)
            } catch (ex: AccountCreationException) {
                _error.emit(ex.kind)
            } catch (th: Throwable) {
                th.printStackTrace()
                _error.emit(CreationError.UNDEFINED)
            }
        }
    }

    fun createHotp(name: String, secret: String, counter: Long) {
        viewModelScope.launch(dispatcher) {
            try {
                val account = manager.createHotpAccount(
                    name,
                    null,
                    secret,
                    Constants.DEFAULT_ALGORITHM,
                    Constants.DEFAULT_DIGITS,
                    counter
                )
                _success.emit(account)
            } catch (ex: AccountCreationException) {
                _error.emit(ex.kind)
            } catch (th: Throwable) {
                th.printStackTrace()
                _error.emit(CreationError.UNDEFINED)
            }
        }
    }
}
