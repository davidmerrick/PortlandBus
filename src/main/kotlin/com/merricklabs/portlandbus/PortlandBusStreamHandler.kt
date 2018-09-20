package com.merricklabs.portlandbus

import com.amazon.ask.SkillStreamHandler

class PortlandBusStreamHandler : SkillStreamHandler {
    constructor() : super(getSkill())

    fun getSkill() {
        val injector = Guice.createInjector(PortlandBusModule())
        val config = injector.getInstance(PortlandBusConfig::class.java)

        return Skills.standard()
                .addRequestHandlers(
                        injector.getInstance(LaunchRequestHandler::class.java),
                        injector.getInstance(SaveStopIntentHandler::class.java),
                        injector.getInstance(GetArrivalsIntentHandler::class.java),
                        injector.getInstance(MyStopIntentHandler::class.java),
                        injector.getInstance(CancelandStopIntentHandler::class.java),
                        injector.getInstance(HelpIntentHandler::class.java),
                        injector.getInstance(SessionEndedRequestHandler::class.java),
                        injector.getInstance(FallbackIntentHandler::class.java)
                )
                .withSkillId(config.getAlexa().getSkillId())
                .build()
    }
}