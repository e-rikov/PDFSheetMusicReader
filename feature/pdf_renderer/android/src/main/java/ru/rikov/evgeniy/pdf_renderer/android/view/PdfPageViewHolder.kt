package ru.rikov.evgeniy.pdf_renderer.android.view

import android.graphics.pdf.PdfRenderer
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.rikov.evgeniy.core.android.recycler_view.AppRecyclerViewHolder
import ru.rikov.evgeniy.core.android.tools.is26orMore
import ru.rikov.evgeniy.pdf_renderer.android.R


class PdfPageViewHolder(
    parent: ViewGroup,
    private val pdfRenderer: PdfRenderer?
) : AppRecyclerViewHolder<Int>(parent, R.layout.pdf_renderer_item_page) {

    private val pageBitmap: ImageView = itemView.findViewById(R.id.pageBitmap)

    
    override fun bind(item: Int) {
        when {
            pdfRenderer == null -> return

            is26orMore() -> GlobalScope.launch(Dispatchers.IO) {
                val bitmap = synchronized(pdfRenderer) {
                    pdfRenderer.openPage(item).renderPage()
                }

                withContext(Dispatchers.Main) {
                    pageBitmap.setImageBitmap(bitmap)
                }
            }

            else -> pdfRenderer.openPage(item)?.apply {
                pageBitmap.setImageBitmap(renderPage())
            }
        }
    }
    
}