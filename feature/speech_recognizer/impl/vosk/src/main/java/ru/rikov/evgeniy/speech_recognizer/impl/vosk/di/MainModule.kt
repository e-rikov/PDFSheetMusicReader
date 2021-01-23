package ru.rikov.evgeniy.speech_recognizer.impl.vosk.di

import org.koin.dsl.module
import ru.rikov.evgeniy.speech_recognizer.impl.vosk.VoskSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer


val speechRecognizerModule = module {
    factory<AppSpeechRecognizer> { VoskSpeechRecognizer(get()) }
}