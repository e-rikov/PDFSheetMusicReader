package ru.rikov.evgeniy.speech_recognizer.main

import kotlinx.coroutines.flow.StateFlow
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


interface AppSpeechRecognizer {

    fun startListening(): StateFlow<RecognitionState>

    fun stopListening()

}