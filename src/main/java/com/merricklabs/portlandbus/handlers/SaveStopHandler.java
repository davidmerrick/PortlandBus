package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.storage.MyStopStorage;
import com.merricklabs.portlandbus.utils.SkillsHelper;
import java.util.Optional;
import java.util.OptionalInt;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static com.amazon.ask.request.Predicates.intentName;
import static com.merricklabs.portlandbus.constants.PortlandBusIntents.SAVE_STOP_INTENT;

@Slf4j
public class SaveStopHandler implements RequestHandler {

    private final PortlandBusConfig config;
    private final SkillsHelper skillsHelper;
    private final MyStopStorage storage;

    @Inject
    public SaveStopHandler(PortlandBusConfig config, SkillsHelper skillsHelper, MyStopStorage myStopStorage) {
        this.config = config;
        this.skillsHelper = skillsHelper;
        this.storage = myStopStorage;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(SAVE_STOP_INTENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        OptionalInt stopIdOptional = skillsHelper.getStopId(input);
        if (!stopIdOptional.isPresent()) {
            return stopRequired(input);
        }
        int stopId = stopIdOptional.getAsInt();
        String userId = input.getRequestEnvelope().getSession().getUser().getUserId();
        try {
            storage.saveStop(userId, stopId);
            return skillsHelper.savedStopResponse(input, stopId);
        } catch (Exception e) {
            return skillsHelper.errorSavingStop(input, stopId);
        }
    }

    private Optional<Response> stopRequired(HandlerInput input) {
        PortlandBusConfig.Alexa alexaConfig = config.getAlexa();

        String speechText = "Please specify a stop to save.";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(alexaConfig.getInvocationName(), speechText)
                .build();
    }
}
