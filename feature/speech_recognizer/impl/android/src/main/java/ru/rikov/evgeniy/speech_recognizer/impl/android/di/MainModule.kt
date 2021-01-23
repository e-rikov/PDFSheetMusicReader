package ru.rikov.evgeniy.speech_recognizer.impl.android.di

import org.koin.dsl.module
import ru.rikov.evgeniy.speech_recognizer.impl.android.AndroidSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer


val speechRecognizerModule = module {
    factory<AppSpeechRecognizer> { AndroidSpeechRecognizer(get()) }
}