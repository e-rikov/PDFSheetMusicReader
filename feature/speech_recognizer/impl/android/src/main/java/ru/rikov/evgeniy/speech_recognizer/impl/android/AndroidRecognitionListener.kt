package ru.rikov.evgeniy.speech_recognizer.impl.android

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.lang.IllegalStateException


class AndroidRecognitionListener(
    private val stateFlow: MutableStateFlow<RecognitionState>
) : RecognitionListener {

    override fun onReadyForSpeech(bundle: Bundle) {}

    override fun onBeginningOfSpeech() {
        stateFlow.value = RecognitionState.BeginningOfSpeech
    }

    override fun onRmsChanged(v: Float) {}

    override fun onBufferReceived(bytes: ByteArray) {}

    override fun onEndOfSpeech() {
        stateFlow.value = RecognitionState.EndOfSpeech
    }

    override fun onError(errorCode: Int) {
        val errorText = when (errorCode) {
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network operation timed out."
            SpeechRecognizer.ERROR_NETWORK -> "Other network related errors."
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
            SpeechRecognizer.ERROR_SERVER -> "Server sends error status."
            SpeechRecognizer.ERROR_CLIENT -> "Other client side errors."
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input."
            SpeechRecognizer.ERROR_NO_MATCH -> "No recognition result matched."
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy."
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions."
            else -> ""
        }

        val error = IllegalStateException(errorText)
        stateFlow.value = RecognitionState.Error(error)
    }

    override fun onResults(bundle: Bundle) {
        val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        stateFlow.value = RecognitionState.Result(data!![0])
    }

    override fun onPartialResults(bundle: Bundle) {
        stateFlow.value = RecognitionState.PartialResult("")
    }

    override fun onEvent(i: Int, bundle: Bundle) {}

}