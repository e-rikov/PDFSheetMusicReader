package ru.rikov.evgeniy.speech_recognizer.impl.sphinx

import android.content.Context
import edu.cmu.pocketsphinx.Assets
import edu.cmu.pocketsphinx.SpeechRecognizer
import edu.cmu.pocketsphinx.SpeechRecognizerSetup
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.schedulers.Schedulers
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.io.File


class SphinxSpeechRecognizer(
    private val context: Context
) : AppSpeechRecognizer {

    private var recognizer: SpeechRecognizer? = null
    private var observable: Observable<RecognitionState>? = null
    private var emitter: ObservableEmitter<RecognitionState>? = null


    override fun startListening(): Observable<RecognitionState> =
        observable ?: initRecognizer().flatMap {
            Observable
                .create<RecognitionState> { emitter ->
                    this.emitter = emitter
                    it.addListener(SphinxRecognitionListener(emitter))
                    it.startListening(COMMANDS_SEARCH)
                }
                .doOnNext { state ->
                    if (state == RecognitionState.EndOfSpeech) {
                        it.stop()
                        it.startListening(COMMANDS_SEARCH)
                    }
                }
                .apply {
                    observable = this
                }
        }

    private fun initRecognizer(): Observable<SpeechRecognizer> =
        Observable
            .fromCallable {
                val assets = Assets(context)
                val assetDir = assets.syncAssets()

                createRecognizer(assetDir).apply {
                    this@SphinxSpeechRecognizer.recognizer = this
                }
            }
            .subscribeOn(Schedulers.io())

    override fun stopListening() {
        recognizer?.also {
            it.cancel()
            it.shutdown()
        }

        recognizer = null

        emitter?.onComplete()
        emitter = null

        observable = null
    }

    private fun createRecognizer(assetsDir: File): SpeechRecognizer {
        val recognizer = SpeechRecognizerSetup.defaultSetup()
            .setAcousticModel(File(assetsDir, "en-us-ptm"))
            .setDictionary(File(assetsDir, "cmudict-en-us.dict"))
            .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
            .recognizer


        val menuGrammar = File(assetsDir, "commands.gram")
        recognizer.addGrammarSearch(
            COMMANDS_SEARCH,
            menuGrammar)

        return recognizer
    }



    companion object {
        private const val COMMANDS_SEARCH = "commands"
    }

}