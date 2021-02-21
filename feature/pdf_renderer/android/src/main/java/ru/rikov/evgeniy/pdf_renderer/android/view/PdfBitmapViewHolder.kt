package ru.rikov.evgeniy.pdf_renderer.android.view

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.ImageView
import ru.rikov.evgeniy.core.android.recycler_view.AppRecyclerViewHolder
import ru.rikov.evgeniy.pdf_renderer.android.R


class PdfBitmapViewHolder(
    parent: ViewGroup
) : AppRecyclerViewHolder<Bitmap?>(parent, R.layout.pdf_renderer_item_page) {

    private val pageBitmap: ImageView = itemView.findViewById(R.id.pageBitmap)


    override fun bind(item: Bitmap?) {
        pageBitmap.setImageBitmap(item)
    }
    
}