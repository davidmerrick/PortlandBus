package com.merricklabs.portlandbus.utils;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.models.ArrivalListPronouncer;
import java.util.Optional;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SkillsHelper {

    private final PortlandBusConfig.Alexa alexaConfig;

    @Inject
    public SkillsHelper(PortlandBusConfig config) {
        this.alexaConfig = config.getAlexa();
    }

    public Optional<Integer> getStopId(HandlerInput input) {
        String slotValue = getSlotValue(input, alexaConfig.getStopIdSlot());
        return slotValue == null ? Optional.empty() : Optional.of(Integer.parseInt(slotValue));
    }

    private String getSlotValue(HandlerInput input, String slotName) throws IllegalArgumentException {
        Slot slot = ((IntentRequest) input.getRequestEnvelope()
                .getRequest())
                .getIntent()
                .getSlots()
                .get(slotName);

        if (slot == null || slot.getValue() == null){
            return null;
        }
        return slot.getValue();
    }

    public Optional<Response> savedStopResponse(HandlerInput input, int stopId) {
        log.info("Success: Saved stop {}", stopId);
        String speechText = "Saved stop " + ArrivalListPronouncer.pronounceStop(stopId) + ".";
        String displayText = "Saved stop " + stopId + ".";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.getInvocationName(), displayText)
                .build();
    }

    public Optional<Response> errorSavingStop(HandlerInput input, int stopId) {
        log.error("Error saving stop {}.", stopId);
        String speechText = "Sorry, there was a problem saving that stop.";
        String displayText = speechText;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.getInvocationName(), displayText)
                .build();
    }
}
