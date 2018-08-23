package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.inject.Inject;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.external.trimet.TrimetClientImpl;
import com.merricklabs.portlandbus.models.ArrivalListPronouncer;
import com.merricklabs.portlandbus.storage.MyStopStorage;
import com.merricklabs.portlandbus.utils.SkillsHelper;
import java.util.Optional;
import java.util.OptionalInt;
import lombok.extern.slf4j.Slf4j;

import static com.amazon.ask.request.Predicates.intentName;
import static com.merricklabs.portlandbus.constants.PortlandBusIntents.MY_STOP_INTENT;

@Slf4j
public class MyStopIntentHandler implements RequestHandler {

    private final TrimetClient trimetClient;
    private final PortlandBusConfig config;
    private final MyStopStorage storage;

    @Inject
    public MyStopIntentHandler(TrimetClient trimetClient, PortlandBusConfig config, MyStopStorage storage) {
        this.trimetClient = trimetClient;
        this.config = config;
        this.storage = storage;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(MY_STOP_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String userId = input.getRequestEnvelope().getSession().getUser().getUserId();
        OptionalInt stopIdQuery = storage.queryStopId(userId);
        if (!stopIdQuery.isPresent()) {
            return noStopSaved(input);
        }

        final int stopId = stopIdQuery.getAsInt();
        try {
            ArrivalListPronouncer pronouncer = new ArrivalListPronouncer(stopId, trimetClient.getArrivalsForStop(stopId));
            String speechText = pronouncer.pronounceArrivals();
            String displayText = pronouncer.showArrivals();
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard(config.getAlexa().getInvocationName(), displayText)
                    .build();
        } catch (IllegalArgumentException e) {
            return errorState(input);
        }
    }

    private Optional<Response> noStopSaved(HandlerInput input) {
        log.info("User has not saved a stop yet.");

        String speechText = "You haven't saved a stop yet. Please save a stop first.";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(config.getAlexa().getInvocationName(), speechText)
                .build();
    }

    private Optional<Response> errorState(HandlerInput input) {
        String speechText = "Sorry, there was a problem getting arrivals for that stop.";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(config.getAlexa().getInvocationName(), speechText)
                .build();
    }
}
