package ru.rikov.evgeniy.pdf_renderer.android

import android.app.Activity
import android.net.Uri
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import ru.rikov.evgeniy.core.main.AppClosable


interface AppPdfRenderer : AppClosable {

    fun init(activity: Activity)
    fun init (activity: Activity, pages: ViewPager2, waiter: View)

    fun hideWaiter()
    fun showWaiter()

    fun scrollToPage(pageNumber: Int)

    /**
     * Loads PDF by Uri asynchronously and returns loaded page count.
     */
    suspend fun showPdf(pdfFileUri: Uri?): Int

    /**
     * Loads PDF by path asynchronously and returns loaded page count.
     */
    suspend fun showPdf(pdfFilePath: String?): Int

}