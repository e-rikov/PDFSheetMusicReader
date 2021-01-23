package ru.rikov.evgeniy.core.android.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView


open class AppRecyclerViewHolder<Item>(
    parent: ViewGroup,

    @LayoutRes
    layoutResId: Int
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
) {

    open fun bind(item: Item) {}

    open fun bind(item: Item, isSelected: Boolean) {}

    open fun onDestroy() {}

    open fun onAttach() {}

    open fun onDetach() {}

}