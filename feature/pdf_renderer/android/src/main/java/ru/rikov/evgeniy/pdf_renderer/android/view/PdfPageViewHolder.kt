package ru.rikov.evgeniy.pdf_renderer.android.view

import android.graphics.pdf.PdfRenderer
import android.view.ViewGroup
import android.widget.ImageView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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

            is26orMore() -> Single
                .fromCallable {
                    synchronized(pdfRenderer) {
                        pdfRenderer.openPage(item).renderPage()
                    }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext { Single.never() }
                .subscribe(pageBitmap::setImageBitmap)

            else -> pdfRenderer.openPage(item)?.apply {
                pageBitmap.setImageBitmap(renderPage())
            }
        }
    }
    
}