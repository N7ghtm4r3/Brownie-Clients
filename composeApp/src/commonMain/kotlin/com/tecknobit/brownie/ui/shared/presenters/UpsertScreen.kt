@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.brownie.ui.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import brownie.composeapp.generated.resources.Res.string
import brownie.composeapp.generated.resources.edit
import brownie.composeapp.generated.resources.save
import com.tecknobit.brownie.navigator
import com.tecknobit.brownie.ui.screens.host.components.SectionTitle
import com.tecknobit.brownie.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.brownie.ui.theme.BrownieTheme
import com.tecknobit.browniecore.helpers.BrownieInputsValidator
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [UpsertScreen] class is useful to handle the insertion or editing of an item
 *
 * @param itemId The identifier of the item to update
 * @param viewModel The support viewmodel for the screen
 *
 * @param T The type of the item displayed
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@Structure
abstract class UpsertScreen<T, V : UpsertScreenViewModel<T>>(
    itemId: String?,
    viewModel: V,
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    /**
     *`isEditing` whether the action is an edit action
     */
    protected val isEditing = itemId != null

    /**
     *`item` the existing item to update
     */
    protected lateinit var item: State<T?>

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        BrownieTheme {
            ManagedContent(
                modifier = Modifier
                    .fillMaxSize(),
                viewModel = viewModel,
                initialDelay = 500,
                loadingRoutine = if (isEditing) {
                    {
                        item.value != null
                    }
                } else
                    null,
                content = {
                    CollectStatesAfterLoading()
                    Scaffold(
                        modifier = Modifier
                            .navigationBarsPadding(),
                        topBar = {
                            MediumTopAppBar(
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                navigationIcon = {
                                    IconButton(
                                        onClick = { navigator.goBack() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                            contentDescription = null
                                        )
                                    }
                                },
                                title = { Title() },
                                actions = { Actions() }
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(
                                    top = it.calculateTopPadding() + 16.dp,
                                    bottom = 16.dp
                                )
                                .padding(
                                    horizontal = 16.dp
                                )
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier
                                    .widthIn(
                                        max = 1000.dp
                                    ),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Form()
                                ButtonsSection()
                            }
                        }
                    }
                }
            )
        }
    }

    /**
     * Custom section used to display the title of the item
     */
    @Composable
    @ScreenSection
    protected abstract fun Title()

    /**
     * Custom section used to provide extra actions available to handle the item
     */
    @Composable
    @ScreenSection
    protected open fun Actions() {
    }

    /**
     * The form used to insert or edit the item details
     */
    @Composable
    @ScreenSection
    protected abstract fun Form()

    /**
     * The section used to allow the user insert the name of the item
     *
     * @param header The representative header of the section
     * @param placeholder The placeholder of the [EquinoxOutlinedTextField]
     * @param errorText The error text to display when the input is not valid
     */
    @Composable
    @ScreenSection
    protected fun ItemNameSection(
        header: StringResource,
        placeholder: StringResource,
        errorText: StringResource,
    ) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        SectionTitle(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            title = header,
            fontSize = 18.sp
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            value = viewModel.name,
            isError = viewModel.nameError,
            validator = { BrownieInputsValidator.isItemNameValid(it) },
            placeholder = placeholder,
            errorText = errorText,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    /**
     * Section containing the buttons to save the data inserted [Form] component
     */
    @Composable
    @ScreenSection
    protected fun ColumnScope.ButtonsSection() {
        Column(
            modifier = Modifier
                .align(Alignment.End),
            horizontalAlignment = Alignment.End
        ) {
            ResponsiveContent(
                onExpandedSizeClass = {
                    ExpandedUpsertButtons()
                },
                onMediumSizeClass = {
                    ExpandedUpsertButtons()
                },
                onCompactSizeClass = {
                    CompactUpsertButtons()
                }
            )
        }
    }

    /**
     * Custom [ButtonsSection] displayed on large screen size classes
     */
    @Composable
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun ExpandedUpsertButtons() {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UpsertButtons(
                modifier = Modifier
            )
        }
    }

    /**
     * Custom [ButtonsSection] displayed on compact screen size classes
     */
    @Composable
    @CompactClassComponent
    @NonRestartableComposable
    private fun CompactUpsertButtons() {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UpsertButtons(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
            )
        }
    }

    /**
     * Custom [Button] used to upsert the data of the [Form] component
     *
     * @param modifier The modifier to apply to the component
     */
    @Composable
    @RequiresSuperCall
    protected open fun UpsertButtons(
        modifier: Modifier,
    ) {
        Button(
            modifier = modifier,
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = { viewModel.upsert() }
        ) {
            Text(
                text = stringResource(
                    if (isEditing)
                        string.edit
                    else
                        string.save
                )
            )
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    /**
     * Method used to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        item = viewModel.item.collectAsState()
        viewModel.nameError = remember { mutableStateOf(false) }
    }

}