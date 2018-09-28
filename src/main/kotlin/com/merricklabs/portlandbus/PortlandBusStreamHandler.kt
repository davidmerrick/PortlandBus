package com.merricklabs.portlandbus

import com.amazon.ask.Skill
import com.amazon.ask.SkillStreamHandler
import com.amazon.ask.Skills
import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.external.trimet.TrimetClientImpl
import com.merricklabs.portlandbus.handlers.*
import com.merricklabs.portlandbus.storage.MyStopStorage
import com.merricklabs.portlandbus.storage.MyStopStorageImpl
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.startKoin

class PortlandBusStreamHandler : SkillStreamHandler {

    companion object {
        val skill: Skill
            get() {
                val portlandBusModule = module {
                    single { PortlandBusConfig() }

                    single { TrimetClientImpl() as TrimetClient }
                    single { MyStopStorageImpl() as MyStopStorage }

                    single { CancelandStopIntentHandler() }
                    single { FallbackIntentHandler() }
                    single { GetArrivalsIntentHandler() }
                    single { HelpIntentHandler() }
                    single { LaunchRequestHandler() }
                    single { MyStopIntentHandler() }
                    single { SaveStopIntentHandler() }
                    single { SessionEndedRequestHandler() }
                }

                startKoin(listOf(portlandBusModule))

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

    constructor() : super(skill)
}