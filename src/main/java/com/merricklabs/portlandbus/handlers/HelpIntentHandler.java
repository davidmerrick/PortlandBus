package com.merricklabs.portlandbus.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import com.google.inject.Inject;
import com.merricklabs.portlandbus.PortlandBusConfig;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {

    private final PortlandBusConfig config;

    @Inject
    public HelpIntentHandler(PortlandBusConfig config) {
        this.config = config;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String repromptText = "Which stop would you like information about?";
        final String INVOCATION_NAME = config.getAlexa().getInvocationName();
        String speechText = new StringBuilder()
                .append("Welcome to " + INVOCATION_NAME + ". ")
                .append("I can retrieve arrival times for bus stops in Portland, Oregon. ")
                .append(repromptText)
                .toString();
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard(INVOCATION_NAME, speechText)
                .withReprompt(repromptText)
                .build();
    }
}
