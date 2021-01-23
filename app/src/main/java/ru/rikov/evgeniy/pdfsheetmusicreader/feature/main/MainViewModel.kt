package ru.rikov.evgeniy.pdfsheetmusicreader.feature.main

import androidx.lifecycle.LiveData


interface MainViewModel {

    val currentPage: LiveData<Int>

    fun init(pagesCount: Int)

}