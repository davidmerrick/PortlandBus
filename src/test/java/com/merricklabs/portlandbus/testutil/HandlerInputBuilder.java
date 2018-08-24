package com.merricklabs.portlandbus.testutil;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.User;
import com.amazon.ask.model.interfaces.system.SystemState;
import com.merricklabs.portlandbus.PortlandBusConfig;
import java.util.Map;
import lombok.Builder;

import static com.merricklabs.portlandbus.constants.PortlandBusIntents.GET_ARRIVALS_INTENT;

/**
 * Helper class for building intent inputs
 */
@Builder
public class HandlerInputBuilder {

    private Map<String, Slot> slots;
    private String userId;
    private String intentName;
    private PortlandBusConfig.Alexa config;

    public HandlerInputBuilder(Map<String, Slot> slots, String userId, String intentName, PortlandBusConfig.Alexa config) {
        this.slots = slots;
        this.userId = userId;
        this.intentName = intentName;
        this.config = config;
    }

    public HandlerInput getHandlerInput() {
        Intent intent = Intent.builder()
                .withName(GET_ARRIVALS_INTENT)
                .withSlots(slots)
                .build();

        IntentRequest intentRequest = IntentRequest.builder()
                .withIntent(intent)
                .build();

        SystemState systemState = SystemState.builder()
                .withUser(User.builder().withUserId(userId).build())
                .build();

        Context context = Context.builder()
                .withSystem(systemState)
                .build();

        RequestEnvelope requestEnvelope = RequestEnvelope.builder()
                .withVersion("1.0")
                .withRequest(intentRequest)
                .withContext(context)
                .build();

        return HandlerInput.builder()
                .withRequestEnvelope(requestEnvelope)
                .build();
    }
}
