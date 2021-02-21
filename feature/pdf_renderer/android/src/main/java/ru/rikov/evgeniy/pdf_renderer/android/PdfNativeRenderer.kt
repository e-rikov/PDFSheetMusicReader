package ru.rikov.evgeniy.pdf_renderer.android

import android.app.Activity
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.rikov.evgeniy.core.android.recycler_view.createAdapter
import ru.rikov.evgeniy.pdf_renderer.android.view.PdfBitmapViewHolder
import ru.rikov.evgeniy.pdf_renderer.android.view.PdfPageViewHolder
import ru.rikov.evgeniy.pdf_renderer.android.view.renderPage
import java.io.File


class PdfNativeRenderer(
    private val fileProviderName: String
) : AppPdfRenderer {

    private var activity: Activity? = null
    private var pages: ViewPager2? = null
    private var pdfRenderer: PdfRenderer? = null
    private var waiter: View? = null


    override fun init(activity: Activity) {
        this.activity = activity
        activity.setContentView(R.layout.pdf_renderer_activity)
        pages = activity.findViewById(R.id.pages)
        waiter = activity.findViewById(R.id.waiter)
    }

    override fun init (activity: Activity, pages: ViewPager2, waiter: View) {
        this.activity = activity
        this.pages = pages
        this.waiter = waiter
    }

    override fun hideWaiter() {
        pages?.isVisible = true
        waiter?.isVisible = false
    }

    override fun showWaiter() {
        pages?.isVisible = false
        waiter?.isVisible = true
    }

    override fun scrollToPage(pageNumber: Int) {
        if (pageNumber < 0) return
        val pages = this.pages ?: return
        val currentPage = pages.currentItem
        val pageCount = pages.adapter?.itemCount ?: return
        if ((currentPage == pageNumber) || (pageCount <= pageNumber)) return
        pages.currentItem = pageNumber
    }

    override suspend fun showPdf(pdfFileUri: Uri?): Int {
        if (pdfFileUri == null) throw createPdfRendererThrowable()

        showWaiter()
        pdfRenderer?.close()
        pdfRenderer = null

        try {
            activity?.grantUriPermission(
                fileProviderName,
                pdfFileUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION)

            var pageCount: Int? = null

            activity?.contentResolver
                ?.openFileDescriptor(pdfFileUri, "r")
                ?.let {
                    pdfRenderer = PdfRenderer(it)
                    pageCount = loadPdfViewerPage()
                }

            return pageCount ?: throw createPdfRendererThrowable()
        } catch (exception: SecurityException) {
            throw createPdfRendererThrowable()
        } finally {
            activity = null
        }
    }

    override suspend fun showPdf(pdfFilePath: String?): Int {
        if (pdfFilePath.isNullOrEmpty()) throw createPdfRendererThrowable()

        showWaiter()

        val pdfFile = File(pdfFilePath)

        return if (pdfFile.exists()) {
            pdfRenderer?.close()

            val input = ParcelFileDescriptor.open(
                pdfFile,
                ParcelFileDescriptor.MODE_READ_ONLY)

            pdfRenderer = PdfRenderer(input)

            loadPdfViewerPage()
        } else {
            throw createPdfRendererThrowable()
        }
    }

    override fun close() {
        pdfRenderer?.close()

        activity = null
        pdfRenderer = null
        pages = null
        waiter = null
    }

    private suspend fun initRecyclerView(): Int {
        val renderer = this.pdfRenderer

        return if (renderer != null) {
            val pageCount = renderer.pageCount

            // Если PDF-ка содержит менее 5 страниц, то в фоне отрисовываем их все и создаём
            // адаптер с айтемами - bitmap-ми, на которых уже готовые отрисованные страницы:
            if (pageCount < 5) {
                renderer.initBitmapPages()
            } else {
                renderer.initPages(pageCount)
            }

            pageCount
        } else {
            throw createPdfRendererThrowable()
        }
    }

    private fun createPdfRendererThrowable() =
        Throwable(activity?.getString(R.string.pdf_renderer_fail_loading))

    private suspend fun PdfRenderer.initBitmapPages() {
        val items = (0 until pageCount).map {
            this.openPage(it)?.renderPage()
        }

        this.close()
        pdfRenderer = null
        items.filterNotNull()

        withContext(Dispatchers.Main) {
            val adapter = items.createAdapter { parent, _ ->
                PdfBitmapViewHolder(parent)
            }

            setRecyclerViewAdapter(adapter)
        }
    }

    private fun PdfRenderer.initPages(pageCount: Int) {
        val items = (0 until pageCount).map { it }

        val adapter = items.createAdapter { parent, _ ->
            PdfPageViewHolder(parent, this)
        }

        setRecyclerViewAdapter(adapter)
    }

    private fun setRecyclerViewAdapter(adapter: RecyclerView.Adapter<*>) {
        pages?.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private suspend fun loadPdfViewerPage(): Int {
        val pageCount = initRecyclerView()
        hideWaiter()
        return pageCount
    }

}