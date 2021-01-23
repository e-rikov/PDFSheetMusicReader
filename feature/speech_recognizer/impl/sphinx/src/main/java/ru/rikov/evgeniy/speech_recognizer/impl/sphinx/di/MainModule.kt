package ru.rikov.evgeniy.speech_recognizer.impl.sphinx.di

import org.koin.dsl.module
import ru.rikov.evgeniy.speech_recognizer.impl.sphinx.SphinxSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer


val speechRecognizerModule = module {
    factory<AppSpeechRecognizer> { SphinxSpeechRecognizer(get()) }
}