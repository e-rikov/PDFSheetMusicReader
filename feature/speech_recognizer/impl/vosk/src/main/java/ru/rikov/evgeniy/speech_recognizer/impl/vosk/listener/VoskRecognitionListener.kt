package ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.kaldi.RecognitionListener
import ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener.model.PartialResult
import ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener.model.Result
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.lang.Exception


internal class VoskRecognitionListener(
    private val stateFlow: MutableStateFlow<RecognitionState>
) : RecognitionListener {

    override fun onPartialResult(hypothesis: String?) {
        hypothesis ?: return
        val result = Json.decodeFromString<PartialResult>(hypothesis)
        stateFlow.value = RecognitionState.PartialResult(result.partial)
    }

    override fun onResult(hypothesis: String?) {
        hypothesis ?: return
        val result = Json.decodeFromString<Result>(hypothesis)
        stateFlow.value = RecognitionState.Result(result.text)
    }

    override fun onError(error: Exception?) {
        val normalizedError = error ?: IllegalStateException("Something wrong with VoskSpeechRecognizer")
        stateFlow.value = RecognitionState.Error(normalizedError)
    }

    override fun onTimeout() {
        stateFlow.value = RecognitionState.EndOfSpeech
    }

}
