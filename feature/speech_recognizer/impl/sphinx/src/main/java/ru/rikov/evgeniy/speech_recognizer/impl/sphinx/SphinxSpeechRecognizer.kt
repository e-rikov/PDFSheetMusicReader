package ru.rikov.evgeniy.speech_recognizer.impl.sphinx

import android.content.Context
import edu.cmu.pocketsphinx.Assets
import edu.cmu.pocketsphinx.SpeechRecognizer
import edu.cmu.pocketsphinx.SpeechRecognizerSetup
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.io.File


class SphinxSpeechRecognizer(
    private val context: Context
) : AppSpeechRecognizer {

    private var recognizer: SpeechRecognizer? = null
    private var stateFlow = MutableStateFlow<RecognitionState>(RecognitionState.Initialized)
    private var job: Job? = null


    override fun startListening(): StateFlow<RecognitionState> {
        GlobalScope.launch(Dispatchers.IO) {
            val recognizer = initRecognizer()
            recognizer.addListener(SphinxRecognitionListener(stateFlow))
            recognizer.startListening(COMMANDS_SEARCH)
        }

        job = GlobalScope.launch(Dispatchers.IO) {
            stateFlow.collect {
                recognizer?.also {
                    it.stop()
                    it.startListening(COMMANDS_SEARCH)
                }
            }
        }

        return stateFlow
    }

    private fun initRecognizer(): SpeechRecognizer {
        val assets = Assets(context)
        val assetDir = assets.syncAssets()

        return createRecognizer(assetDir).apply {
            this@SphinxSpeechRecognizer.recognizer = this
        }
    }

    override fun stopListening() {
        recognizer?.also {
            it.cancel()
            it.shutdown()
        }

        recognizer = null

        job?.cancel()
        job = null
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