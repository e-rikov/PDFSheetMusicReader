package ru.rikov.evgeniy.pdfsheetmusicreader.di

import android.content.Context
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.rikov.evgeniy.core.android.tools.appPackageName
import ru.rikov.evgeniy.pdf_renderer.android.AppPdfRenderer
import ru.rikov.evgeniy.pdf_renderer.android.PdfNativeRenderer
import ru.rikov.evgeniy.pdfsheetmusicreader.feature.main.MainViewModelImpl


val mainModule = module {
    single {
        val context: Context = get()
        "${context.appPackageName}.provider"
    }
}

val mainScreenModule = module {
    factory<AppPdfRenderer> {
        PdfNativeRenderer(get())
    }

    viewModel { MainViewModelImpl(get()) }
}