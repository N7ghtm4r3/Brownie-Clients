package com.tecknobit.brownie.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import com.tecknobit.browniecore.helpers.BrownieInputsValidator.isItemNameValid
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * The `UpsertScreenViewModel` class is the support class used by the screens which insert new items
 * or edit the existing ones
 *
 * @param itemId The identifier of the item to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 *
 * @param T The type of the item updated
 *
 */
@Structure
abstract class UpsertScreenViewModel<T>(
    protected val itemId: String?,
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     *`_item` the item to update
     */
    protected val _item = MutableStateFlow<T?>(
        value = null
    )
    val item = _item.asStateFlow()

    /**
     * `name` the name of the item
     */
    lateinit var name: MutableState<String>

    /**
     * `nameError` whether the [name] field is not valid
     */
    lateinit var nameError: MutableState<Boolean>

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    /**
     * Method used to retrieve the information of the item to display
     */
    abstract fun retrieveItem()

    /**
     * Method used to insert or update an item
     */
    fun upsert() {
        if (!validForm())
            return
        if (itemId == null)
            insert()
        else
            update()
    }

    /**
     * Method used to insert a new item
     */
    protected abstract fun insert()

    /**
     * Method used to edit an existing item
     */
    protected abstract fun update()

    /**
     * Method used to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
    @RequiresSuperCall
    protected open fun validForm(): Boolean {
        if (!isItemNameValid(name.value)) {
            nameError.value = true
            return false
        }
        return true
    }

}