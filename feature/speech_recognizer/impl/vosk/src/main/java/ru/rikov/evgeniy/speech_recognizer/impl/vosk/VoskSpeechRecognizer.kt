package ru.rikov.evgeniy.speech_recognizer.impl.vosk

import android.content.Context
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import org.kaldi.Assets
import org.kaldi.KaldiRecognizer
import org.kaldi.Model
import org.kaldi.SpeechService
import ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener.VoskRecognitionListener
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState


class VoskSpeechRecognizer(
    private val context: Context
) : AppSpeechRecognizer {

    private var speechService: SpeechService? = null
    private var observable: Observable<RecognitionState>? = null
    private var emitter: ObservableEmitter<RecognitionState>? = null

    override fun startListening(): Observable<RecognitionState> =
        observable ?: initSpeechService().flatMap {
            Observable
                .create<RecognitionState> { emitter ->
                    this.emitter = emitter
                    it.addListener(VoskRecognitionListener(emitter))
                    it.startListening()
                }
                .apply {
                    observable = this
                }
        }

    private fun initSpeechService(): Observable<SpeechService> =
        Observable
            .fromCallable {
                val assets = Assets(context)
                val assetDir = assets.syncAssets()
                val model = Model("$assetDir/model-android")
                val recognizer = KaldiRecognizer(model, SAMPLE_RATE)

                SpeechService(recognizer, SAMPLE_RATE).apply {
                    speechService = this
                }
            }
            .subscribeOn(Schedulers.io())

    override fun stopListening() {
        speechService?.also {
            it.cancel()
            it.shutdown()
        }

        speechService = null

        emitter?.onComplete()
        emitter = null

        observable = null
    }



    companion object {

        private const val SAMPLE_RATE = 16000.0f

    }

}