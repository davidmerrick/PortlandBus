package com.merricklabs.portlandbus.util

import com.amazon.ask.dispatcher.request.handler.HandlerInput
import com.amazon.ask.model.IntentRequest
import com.amazon.ask.model.Response
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.models.ArrivalListPronouncer
import mu.KotlinLogging
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.Optional
import java.util.OptionalInt

private val log = KotlinLogging.logger {}

class SkillsHelper : KoinComponent {

    val config by inject<PortlandBusConfig>()

    private val alexaConfig: PortlandBusConfig.Alexa

    init {
        this.alexaConfig = config.alexa
    }

    fun getStopId(input: HandlerInput): OptionalInt {
        val slotValue = getSlotValue(input, alexaConfig.stopIdSlot)
        return if (slotValue == null) OptionalInt.empty() else OptionalInt.of(Integer.parseInt(slotValue))
    }

    @Throws(IllegalArgumentException::class)
    private fun getSlotValue(input: HandlerInput, slotName: String): String? {
        val slot = (input.requestEnvelope
                .request as IntentRequest)
                .intent
                .slots[slotName]

        return if (slot == null || slot.value == null) {
            null
        } else slot.value
    }

    fun savedStopResponse(input: HandlerInput, stopId: Int): Optional<Response> {
        log.info("Success: Saved stop $stopId")
        val speechText = "Saved stop ${ArrivalListPronouncer.pronounceStop(stopId)}."
        val displayText = "Saved stop $stopId."
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.invocationName, displayText)
                .build()
    }

    fun errorSavingStop(input: HandlerInput, stopId: Int): Optional<Response> {
        log.error("Error saving stop $stopId.")
        val speechText = "Sorry, there was a problem saving that stop."
        return input.responseBuilder
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.invocationName, speechText)
                .build()
    }
}
