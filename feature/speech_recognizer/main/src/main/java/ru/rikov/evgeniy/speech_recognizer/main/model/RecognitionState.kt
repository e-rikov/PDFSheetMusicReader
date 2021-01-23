package ru.rikov.evgeniy.speech_recognizer.main.model


sealed class RecognitionState {

    object BeginningOfSpeech : RecognitionState()

    object EndOfSpeech : RecognitionState()

    class PartialResult(val result: String) : RecognitionState()

    class Result(val result: String) : RecognitionState()

    class Error(val error: Throwable) : RecognitionState()

}