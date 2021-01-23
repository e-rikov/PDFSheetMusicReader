package ru.rikov.evgeniy.pdf_renderer.android.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfRenderer


/**
 * Рендерит страницу в Bitmap, после чего страница будет закрыта.
 */
fun PdfRenderer.Page.renderPage(): Bitmap {
    val bitmap = Bitmap.createBitmap(
        width * 2,
        height * 2,
        Bitmap.Config.ARGB_8888)
    
    Canvas(bitmap).apply {
        drawColor(Color.WHITE)
        drawBitmap(bitmap, 0f, 0f, null)
    }
    
    render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    close()
    
    return bitmap
}