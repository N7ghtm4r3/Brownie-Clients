package com.tecknobit.brownie.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isItemNameValid
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Structure
abstract class UpsertScreenViewModel<T>(
    protected val itemId: String?,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _item = MutableStateFlow<T?>(
        value = null
    )
    val item = _item.asStateFlow()

    lateinit var name: MutableState<String>

    lateinit var nameError: MutableState<Boolean>

    abstract fun retrieveItem()

    fun upsert() {
        if (!validForm())
            return
        if (itemId == null)
            insert()
        else
            update()
    }

    protected abstract fun insert()

    protected abstract fun update()

    @RequiresSuperCall
    protected open fun validForm(): Boolean {
        if (!isItemNameValid(name.value)) {
            nameError.value = true
            return false
        }
        return true
    }

}