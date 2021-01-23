package ru.rikov.evgeniy.core.android.recycler_view

import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView


abstract class AppRecyclerViewAdapter<Item>(
    items: List<Item>? = null
) : RecyclerView.Adapter<AppRecyclerViewHolder<Item>>() {
    
    val items = ArrayList<Item>()
    
    
    
    init {
        items?.also { this.items.addAll(it) }
    }
    
    
    
    override fun getItemCount() = items.size
    
    override fun onBindViewHolder(holder: AppRecyclerViewHolder<Item>, position: Int) {
        val item = items[position]
        holder.bind(item)
    }
    
    override fun onViewAttachedToWindow(holder: AppRecyclerViewHolder<Item>) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }
    
    override fun onViewDetachedFromWindow(holder: AppRecyclerViewHolder<Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }
    
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        recyclerView.forEach {
            (recyclerView.getChildViewHolder(it) as? AppRecyclerViewHolder<*>)?.onDestroy()
        }
    }
    
    open fun setItems(newItems: List<Item>?) {
        items.clear()
        newItems?.also { items.addAll(it) }
        notifyDataSetChanged()
    }
    
}



inline fun <reified Item> List<Item>.createAdapter(
    crossinline creator: (parent: ViewGroup, type: Int) -> AppRecyclerViewHolder<Item>
): AppRecyclerViewAdapter<Item> {
    return object : AppRecyclerViewAdapter<Item>(this) {
        override fun onCreateViewHolder(parent: ViewGroup, type: Int) = creator(parent, type)
    }
}

inline fun <reified Item> createAdapter(
    crossinline creator: (parent: ViewGroup, type: Int) -> AppRecyclerViewHolder<Item>
): AppRecyclerViewAdapter<Item> {
    return emptyList<Item>().createAdapter(creator)
}

inline fun <reified Item> List<Item>.createAdapter(
    crossinline creator: (parent: ViewGroup) -> AppRecyclerViewHolder<Item>
): AppRecyclerViewAdapter<Item> {
    return object : AppRecyclerViewAdapter<Item>(this) {
        override fun onCreateViewHolder(parent: ViewGroup, type: Int) = creator(parent)
    }
}

inline fun <reified Item> createAdapter(
    crossinline creator: (parent: ViewGroup) -> AppRecyclerViewHolder<Item>
): AppRecyclerViewAdapter<Item> {
    return emptyList<Item>().createAdapter(creator)
}