package com.merricklabs.portlandbus

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.merricklabs.portlandbus.handlers.*
import org.koin.standalone.StandAloneContext.startKoin

class PortlandBusStreamHandler : SkillStreamHandler(skill) {

    companion object {
        val skill: Skill
            get() {
                startKoin(listOf(PortlandBusModule))

                return Skills.standard()
                        .addRequestHandlers(
                                CancelandStopIntentHandler(),
                                FallbackIntentHandler(),
                                GetArrivalsIntentHandler(),
                                HelpIntentHandler(),
                                LaunchRequestHandler(),
                                MyStopIntentHandler(),
                                SaveStopIntentHandler(),
                                SessionEndedRequestHandler()
                        )
                        .withSkillId(PortlandBusConfig().alexa.skillId)
                        .build()
            }
    }
}