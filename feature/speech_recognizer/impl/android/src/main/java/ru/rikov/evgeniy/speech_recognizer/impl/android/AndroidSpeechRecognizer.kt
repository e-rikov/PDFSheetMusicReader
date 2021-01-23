package ru.rikov.evgeniy.speech_recognizer.impl.android

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.util.*


class AndroidSpeechRecognizer(context: Context) : AppSpeechRecognizer {

    private val speechRecognizer = createSpeechRecognizer(context)
    private var observable: Observable<RecognitionState>? = null
    private var emitter: ObservableEmitter<RecognitionState>? = null


    override fun startListening(): Observable<RecognitionState> {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())

        val observable = this.observable ?:
            Observable
                .create<RecognitionState> { emitter ->
                    this.emitter = emitter
                    speechRecognizer.setRecognitionListener(AndroidRecognitionListener(emitter))
                }
                .doOnNext {
                    startListening()
                }
                .apply {
                    this@AndroidSpeechRecognizer.observable = this
                }

        speechRecognizer.startListening(speechRecognizerIntent)

        return observable
    }

    override fun stopListening() {
        speechRecognizer.stopListening()

        emitter?.onComplete()
        emitter = null

        observable = null
    }

}