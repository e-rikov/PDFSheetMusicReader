package ru.rikov.evgeniy.speech_recognizer.impl.sphinx

import edu.cmu.pocketsphinx.Hypothesis
import edu.cmu.pocketsphinx.RecognitionListener
import kotlinx.coroutines.flow.MutableStateFlow
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


class SphinxRecognitionListener(
    private val stateFlow: MutableStateFlow<RecognitionState>
) : RecognitionListener {

    override fun onBeginningOfSpeech() {
        stateFlow.value = RecognitionState.BeginningOfSpeech
    }

    override fun onEndOfSpeech() {
        stateFlow.value = RecognitionState.EndOfSpeech
    }

    override fun onPartialResult(hypothesis: Hypothesis?) {
        hypothesis ?: return
        stateFlow.value = RecognitionState.PartialResult(hypothesis.hypstr)
    }

    override fun onResult(hypothesis: Hypothesis?) {
        hypothesis ?: return
        stateFlow.value = RecognitionState.Result(hypothesis.hypstr)
    }

    override fun onError(error: Exception?) {
        val normalizedError = error ?: IllegalStateException("Something wrong with SphinxSpeechRecognizer")
        stateFlow.value = RecognitionState.Error(normalizedError)
    }

    override fun onTimeout() {
        stateFlow.value = RecognitionState.EndOfSpeech
    }

}