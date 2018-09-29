package com.merricklabs.portlandbus.handlers

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.dispatcher.request.handler.RequestHandler
import com.amazon.ask.model.Response
import com.amazon.ask.request.Predicates.intentName
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.constants.PortlandBusIntents.SAVE_STOP_INTENT
import com.merricklabs.portlandbus.storage.MyStopStorage
import com.merricklabs.portlandbus.util.SkillsHelper
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.Optional

class SaveStopIntentHandler : RequestHandler, KoinComponent {

    private val config by inject<PortlandBusConfig>()
    private val skillsHelper by inject<SkillsHelper>()
    private val storage by inject<MyStopStorage>()

    override fun canHandle(input: HandlerInput): Boolean {
        return input.matches(intentName(SAVE_STOP_INTENT))
    }

    override fun handle(input: HandlerInput): Optional<Response> {
        val stopIdOptional = skillsHelper.getStopId(input)
        if (!stopIdOptional.isPresent) {
            return stopRequired(input)
        }
        val stopId = stopIdOptional.asInt
        val userId = input.requestEnvelope.session.user.userId
        return try {
            storage.saveStop(userId, stopId)
            skillsHelper.savedStopResponse(input, stopId)
        } catch (e: Exception) {
            skillsHelper.errorSavingStop(input, stopId)
        }
    }

    private fun stopRequired(input: HandlerInput): Optional<Response> {
        val alexaConfig = config.alexa

        val speechText = "Please specify a stop to save."
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.invocationName, speechText)
                .build()
    }
}
