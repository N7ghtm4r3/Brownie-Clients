package com.tecknobit.equinoxcompose.utilities.context

import android.content.Context
import androidx.startup.Initializer

// TODO: TO REMOVE
@Deprecated("WILL BE REMOVED")
class AppContextInitializer : Initializer<Context> {

    override fun create(
        context: Context,
    ): Context {
        AppContext.setUp(context.applicationContext)
        return AppContext.get()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

}

