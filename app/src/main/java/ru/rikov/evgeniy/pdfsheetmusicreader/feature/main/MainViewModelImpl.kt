package ru.rikov.evgeniy.pdfsheetmusicreader.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


class MainViewModelImpl(
    private val speechRecognizer: AppSpeechRecognizer
) : ViewModel(), MainViewModel {

    override val currentPage = MutableStateFlow(0)

    private var pagesCount: Int = 0



    init {
        listen()
    }
    

    
    override fun init(pagesCount: Int) {
        this.pagesCount = pagesCount
        currentPage.value = 0
    }

    private fun listen() {
        viewModelScope.launch {
            speechRecognizer.startListening().collect(::handleResult)
        }
    }
    
    private fun handleResult(state: RecognitionState) {
        if (state is RecognitionState.Result) {
            when (state.result) {
                "вперёд",
                "далее",
                "дальше",
                "следующая страница",
                "next",
                "next page" -> scrollToNextPage()

                "назад",
                "предыдущая страница",
                "prev",
                "previous",
                "previous page" -> scrollToPrevPage()

                "в начало",
                "начало",
                "сначала",
                "to the beginning",
                "to beginning",
                "to start",
                "beginning",
                "start",
                "restart" -> scrollToBeginning()

                "в конец",
                "конец",
                "end",
                "finish",
                "to the end" -> scrollToEnd()
            }
        }
    }

    private fun scrollToNextPage() {
        val nextItemIndex = currentPage.value + 1

        if (nextItemIndex < pagesCount) {
            currentPage.value = nextItemIndex
        }
    }

    private fun scrollToPrevPage() {
        val prevItemIndex = currentPage.value - 1

        if (0 <= prevItemIndex) {
            currentPage.value = prevItemIndex
        }
    }

    private fun scrollToBeginning() {
        currentPage.value = 0
    }

    private fun scrollToEnd() {
        val lastPageIndex = pagesCount - 1

        if (0 < lastPageIndex) {
            currentPage.value = lastPageIndex
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        speechRecognizer.stopListening()
    }

}