package ru.rikov.evgeniy.speech_recognizer.impl.vosk.listener.model

import kotlinx.serialization.Serializable


@Serializable
internal data class Result(
    val result: List<Data>,
    val text: String
) {

    @Serializable
    internal data class Data(
        val conf: Double,
        val end: Double,
        val start: Double,
        val word: String
    )

}
