package ru.rikov.evgeniy.core.android.tools

import android.os.Build

/**
 * Is Lollipop or more
 */
fun is21orMore() = Build.VERSION_CODES.LOLLIPOP <= Build.VERSION.SDK_INT

/**
 * Is Lollipop_1 or more
 */
fun is22orMore() = Build.VERSION_CODES.LOLLIPOP_MR1 <= Build.VERSION.SDK_INT

/**
 * Is Marshmallow or more
 */
fun is23orMore() = Build.VERSION_CODES.M <= Build.VERSION.SDK_INT

/**
 * Is Nougat or more
 */
fun is24orMore() = Build.VERSION_CODES.N <= Build.VERSION.SDK_INT

/**
 * Is Nougat_1 or more
 */
fun is25orMore() = Build.VERSION_CODES.N_MR1 <= Build.VERSION.SDK_INT

/**
 * Is Oreo or more
 */
fun is26orMore() = Build.VERSION_CODES.O <= Build.VERSION.SDK_INT

/**
 * Is Oreo_1 or more
 */
fun is27orMore() = Build.VERSION_CODES.O_MR1 <= Build.VERSION.SDK_INT