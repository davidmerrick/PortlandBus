package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.intentName
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.constants.PortlandBusIntents.GET_ARRIVALS_INTENT
import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.models.ArrivalListPronouncer
import com.merricklabs.portlandbus.util.SkillsHelper
import lombok.extern.slf4j.Slf4j
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.*

@Slf4j
class GetArrivalsIntentHandler : RequestHandler, KoinComponent {

    val config by inject<PortlandBusConfig>()
    val trimetClient by inject<TrimetClient>()
    val skillsHelper by inject<SkillsHelper>()

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(intentName(GET_ARRIVALS_INTENT))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        try {
            val stopId = skillsHelper.getStopId(input).asInt
            val pronouncer = ArrivalListPronouncer(stopId, trimetClient.getArrivalsForStop(stopId))
            val speechText = pronouncer.pronounceArrivals()
            val displayText = pronouncer.showArrivals()

            return input.responseBuilder
                    .withSpeech(speechText)
                    .withSimpleCard(config.alexa.invocationName, displayText)
                    .build()
        } catch (e: Exception) {
            val speechText = "Sorry, there was a problem getting arrivals for that stop."
            return input.responseBuilder
                    .withSpeech(speechText)
                    .withSimpleCard(config.alexa.invocationName, speechText)
                    .build()
        }
    }
}
