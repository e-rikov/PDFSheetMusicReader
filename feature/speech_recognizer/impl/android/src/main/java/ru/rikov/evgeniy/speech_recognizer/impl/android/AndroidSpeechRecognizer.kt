package ru.rikov.evgeniy.speech_recognizer.impl.android

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.rikov.evgeniy.speech_recognizer.main.AppSpeechRecognizer
import ru.rikov.evgeniy.speech_recognizer.main.model.RecognitionState
import java.util.*


class AndroidSpeechRecognizer(context: Context) : AppSpeechRecognizer {

    private val speechRecognizer = createSpeechRecognizer(context)
    private var stateFlow = MutableStateFlow<RecognitionState>(RecognitionState.Initialized)
    private var job: Job? = null


    override fun startListening(): StateFlow<RecognitionState> {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)

        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault())

        if (job == null) {
            val recognitionListener = AndroidRecognitionListener(stateFlow)
            speechRecognizer.setRecognitionListener(recognitionListener)

            job = GlobalScope.launch(Dispatchers.Main) {
                stateFlow.collect {
                    startListening()
                }
            }
        }

        speechRecognizer.startListening(speechRecognizerIntent)

        return stateFlow
    }

    override fun stopListening() {
        speechRecognizer.stopListening()

        job?.cancel()
        job = null
    }

}