package ru.rikov.evgeniy.core.android.tools

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.core.view.size


inline val ViewGroup.firstChild: View?
    get() = if (isNotEmpty()) getChildAt(0) else null

inline val ViewGroup.lastChild: View?
    get() = if (isNotEmpty()) getChildAt(size - 1) else null

fun Activity.getRootViewGroup(): ViewGroup {
    return findViewById(android.R.id.content)
}

@Suppress("UNCHECKED_CAST")
@JvmName("getLastChildTyped")
inline fun <reified T : View> ViewGroup.getLastChild(): T? {
    val child = lastChild
    return if (child is T) child else null
}

inline fun ViewGroup.forEach(crossinline action: (View) -> Unit) {
    val childCount = this.childCount
    
    for (i in 0 until childCount) {
        getChildAt(i)?.also(action)
    }
}

inline fun ViewGroup.forEachIndexed(crossinline action: (Int, View) -> Unit) {
    val childCount = this.childCount
    
    for (i in 0 until childCount) {
        getChildAt(i)?.also { action(i, it) }
    }
}