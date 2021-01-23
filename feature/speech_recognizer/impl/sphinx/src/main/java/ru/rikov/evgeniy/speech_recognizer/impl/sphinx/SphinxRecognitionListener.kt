package ru.rikov.evgeniy.speech_recognizer.impl.sphinx

import edu.cmu.pocketsphinx.Hypothesis
import edu.cmu.pocketsphinx.RecognitionListener
import io.reactivex.ObservableEmitter
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


class SphinxRecognitionListener(
    private val emitter: ObservableEmitter<RecognitionState>
) : RecognitionListener {

    override fun onBeginningOfSpeech() {
        emitter.onNext(RecognitionState.BeginningOfSpeech)
    }

    override fun onEndOfSpeech() {
        emitter.onNext(RecognitionState.EndOfSpeech)
    }

    override fun onPartialResult(hypothesis: Hypothesis?) {
        hypothesis ?: return
        emitter.onNext(RecognitionState.PartialResult(hypothesis.hypstr))
    }

    override fun onResult(hypothesis: Hypothesis?) {
        hypothesis ?: return
        emitter.onNext(RecognitionState.Result(hypothesis.hypstr))
    }

    override fun onError(error: Exception?) {
        val normalizedError = error ?: IllegalStateException("Something wrong with SphinxSpeechRecognizer")
        emitter.onNext(RecognitionState.Error(normalizedError))
    }

    override fun onTimeout() {
        emitter.onNext(RecognitionState.EndOfSpeech)
    }

}