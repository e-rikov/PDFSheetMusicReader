package ru.rikov.evgeniy.speech_recognizer.impl.vosk

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
    private var stateFlow = MutableStateFlow<RecognitionState>(RecognitionState.Initialized)
    private var job: Job? = null

    override fun startListening(): StateFlow<RecognitionState> {
        GlobalScope.launch(Dispatchers.IO) {
            val speechService = initSpeechService()
            speechService.addListener(VoskRecognitionListener(stateFlow))
            speechService.startListening()
        }

        return stateFlow
    }

    private fun initSpeechService(): SpeechService {
        val assets = Assets(context)
        val assetDir = assets.syncAssets()
        val model = Model("$assetDir/model-android")
        val recognizer = KaldiRecognizer(model, SAMPLE_RATE)

        return SpeechService(recognizer, SAMPLE_RATE).apply {
            speechService = this
        }
    }

    override fun stopListening() {
        speechService?.also {
            it.cancel()
            it.shutdown()
        }

        speechService = null

        job?.cancel()
        job = null
    }



    companion object {

        private const val SAMPLE_RATE = 16000.0f

    }

}