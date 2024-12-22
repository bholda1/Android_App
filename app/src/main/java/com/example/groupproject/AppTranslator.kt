package com.example.groupproject

import android.content.Context
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslatorOptions

class AppTranslator(private val context : Context ) {

    private var englishSpanishTranslator : Translator? = null

    fun initializeTranslator( trgLanguage: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage( TranslateLanguage.ENGLISH )
            .setTargetLanguage( trgLanguage )
            .build()
        englishSpanishTranslator = com.google.mlkit.nl.translate.Translation.getClient( options )
    }

    fun downloadModel(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        englishSpanishTranslator?.downloadModelIfNeeded()
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { e -> onFailure("Model download failed: ${e.message}") }
    }

    fun translateText(inputText: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        englishSpanishTranslator?.translate(inputText)
            ?.addOnSuccessListener { translatedText -> onSuccess(translatedText) }
            ?.addOnFailureListener { e -> onFailure("Translation failed: ${e.message}") }
    }

    fun closeTranslator() {
        englishSpanishTranslator?.close()
    }
}