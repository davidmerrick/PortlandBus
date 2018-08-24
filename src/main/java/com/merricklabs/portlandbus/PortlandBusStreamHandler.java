package com.merricklabs.portlandbus;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.merricklabs.portlandbus.handlers.CancelandStopIntentHandler;
import com.merricklabs.portlandbus.handlers.FallbackIntentHandler;
import com.merricklabs.portlandbus.handlers.GetArrivalsIntentHandler;
import com.merricklabs.portlandbus.handlers.HelpIntentHandler;
import com.merricklabs.portlandbus.handlers.LaunchRequestHandler;
import com.merricklabs.portlandbus.handlers.MyStopIntentHandler;
import com.merricklabs.portlandbus.handlers.SaveStopIntentHandler;
import com.merricklabs.portlandbus.handlers.SessionEndedRequestHandler;

public class PortlandBusStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        Injector injector = Guice.createInjector(new PortlandBusModule());
        PortlandBusConfig config = injector.getInstance(PortlandBusConfig.class);

        return Skills.standard()
                .addRequestHandlers(
                        injector.getInstance(LaunchRequestHandler.class),
                        injector.getInstance(SaveStopIntentHandler.class),
                        injector.getInstance(GetArrivalsIntentHandler.class),
                        injector.getInstance(MyStopIntentHandler.class),
                        injector.getInstance(CancelandStopIntentHandler.class),
                        injector.getInstance(HelpIntentHandler.class),
                        injector.getInstance(SessionEndedRequestHandler.class),
                        injector.getInstance(FallbackIntentHandler.class)
                )
                .withSkillId(config.getAlexa().getSkillId())
                .build();
    }

    public PortlandBusStreamHandler() {
        super(getSkill());
    }
}