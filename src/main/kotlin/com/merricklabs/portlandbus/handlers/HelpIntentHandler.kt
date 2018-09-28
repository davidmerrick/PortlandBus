package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.intentName
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.constants.PortlandBusIntents.HELP_INTENT
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

class HelpIntentHandler : RequestHandler, KoinComponent {

    private val config by inject<PortlandBusConfig>()

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(intentName(HELP_INTENT))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        val repromptText = "Which stop would you like information about?"
        val INVOCATION_NAME = config.alexa.invocationName
        val speechText = StringBuilder()
                .append("Welcome to $INVOCATION_NAME. ")
                .append("I can retrieve arrival times for bus stops in Portland, Oregon. ")
                .append(repromptText)
                .toString()
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(INVOCATION_NAME, speechText)
                .withReprompt(repromptText)
                .build()
    }
}
