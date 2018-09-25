package com.merricklabs.portlandbus

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.google.inject.Guice
import com.merricklabs.portlandbus.handlers.CancelandStopIntentHandler

class PortlandBusStreamHandler : SkillStreamHandler {

    companion object {
        val skill: Skill
            get() {
                val injector = Guice.createInjector(PortlandBusModule())
                val config = injector.getInstance(PortlandBusConfig::class)

                Skills.standard()
                        .addRequestHandlers(
                                injector.getInstance<CancelandStopIntentHandler>(CancelandStopIntentHandler::class)
                        )
                        .withSkillId(config.getAlexa().getSkillId())
                        .build()
            }
    }

    constructor() : super(skill)
}