package ru.rikov.evgeniy.pdfsheetmusicreader.feature.main

import kotlinx.coroutines.flow.StateFlow


interface MainViewModel {

    val currentPage: StateFlow<Int>

    fun init(pagesCount: Int)

}