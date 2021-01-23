package ru.rikov.evgeniy.core.android.tools

import android.content.Context


val Context.appPackageName: String
    get() = applicationContext.packageName