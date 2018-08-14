package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.google.inject.Inject;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.models.ArrivalListPronouncer;
import com.merricklabs.portlandbus.utils.SkillsHelper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

import static com.amazon.ask.request.Predicates.intentName;
import static com.merricklabs.portlandbus.constants.PortlandBusIntents.GET_ARRIVALS_INTENT;

@Slf4j
public class GetArrivalsIntentHandler implements RequestHandler {

    private final TrimetClient trimetClient;
    private final PortlandBusConfig config;
    private final SkillsHelper skillsHelper;

    @Inject
    public GetArrivalsIntentHandler(TrimetClient trimetClient, PortlandBusConfig config, SkillsHelper skillsHelper) {
        this.trimetClient = trimetClient;
        this.config = config;
        this.skillsHelper = skillsHelper;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(GET_ARRIVALS_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        try {
            int stopId = skillsHelper.getStopId(input).get();
            ArrivalListPronouncer pronouncer = new ArrivalListPronouncer(stopId, trimetClient.getArrivalsForStop(stopId));
            String speechText = pronouncer.pronounceArrivals();
            String displayText = pronouncer.showArrivals();

            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard(config.getAlexa().getInvocationName(), displayText)
                    .build();
        } catch(Exception e){
            String speechText = "Sorry, there was a problem getting arrivals for that stop.";
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard(config.getAlexa().getInvocationName(), speechText)
                    .build();
        }
    }
}
