package ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener.model

import kotlinx.serialization.Serializable


@Serializable
internal data class PartialResult(val partial: String)