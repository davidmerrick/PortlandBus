package com.merricklabs.portlandbus

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.merricklabs.portlandbus.handlers.CancelandStopIntentHandler
import com.merricklabs.portlandbus.handlers.FallbackIntentHandler
import com.merricklabs.portlandbus.handlers.GetArrivalsIntentHandler
import com.merricklabs.portlandbus.handlers.HelpIntentHandler
import com.merricklabs.portlandbus.handlers.LaunchRequestHandler
import com.merricklabs.portlandbus.handlers.MyStopIntentHandler
import com.merricklabs.portlandbus.handlers.SaveStopIntentHandler
import com.merricklabs.portlandbus.handlers.SessionEndedRequestHandler
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