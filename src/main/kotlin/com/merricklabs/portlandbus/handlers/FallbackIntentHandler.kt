package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.intentName
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.constants.PortlandBusIntents.FALLBACK_INTENT
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

// 2018-July-09: AMAZON.FallackIntent is only currently available in en-US locale.
//              This handler will not be triggered except in that locale, so it can be
//              safely deployed for any locale.
class FallbackIntentHandler : RequestHandler, KoinComponent {

    private val config by inject<PortlandBusConfig>()

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(intentName(FALLBACK_INTENT))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        val speechText = "Sorry, I don't know that. You can say try saying help!"
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(config.alexa.invocationName, speechText)
                .withReprompt(speechText)
                .build()
    }

}
