package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.intentName
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.constants.PortlandBusIntents.MY_STOP_INTENT
import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.models.ArrivalListPronouncer
import com.merricklabs.portlandbus.storage.MyStopStorage
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.Optional

class MyStopIntentHandler : RequestHandler, KoinComponent {

    private val config by inject<PortlandBusConfig>()
    private val storage by inject<MyStopStorage>()
    private val trimetClient by inject<TrimetClient>()

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(intentName(MY_STOP_INTENT))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        val userId = input.requestEnvelope.session.user.userId
        val stopIdQuery = storage.queryStopId(userId)
        if (!stopIdQuery.isPresent) {
            return noStopSaved(input)
        }

        val stopId = stopIdQuery.asInt
        try {
            val pronouncer = ArrivalListPronouncer(stopId, trimetClient.getArrivalsForStop(stopId))
            val speechText = pronouncer.pronounceArrivals()
            val displayText = pronouncer.showArrivals()
            return input.responseBuilder
                    .withSpeech(speechText)
                    .withSimpleCard(config.alexa.invocationName, displayText)
                    .build()
        } catch (e: IllegalArgumentException) {
            return errorState(input)
        }
    }

    private fun noStopSaved(input: HandlerInput): Optional<Response> {
        val speechText = "You haven't saved a stop yet. Please save a stop first."
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(config.alexa.invocationName, speechText)
                .build()
    }

    private fun errorState(input: HandlerInput): Optional<Response> {
        val speechText = "Sorry, there was a problem getting arrivals for that stop."
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(config.alexa.invocationName, speechText)
                .build()
    }
}
