package ru.rikov.evgeniy.speech_recognizer.main

import io.reactivex.Observable
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


interface AppSpeechRecognizer {

    fun startListening(): Observable<RecognitionState>

    fun stopListening()

}