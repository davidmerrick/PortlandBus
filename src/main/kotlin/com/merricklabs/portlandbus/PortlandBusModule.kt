package com.merricklabs.portlandbus

import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.external.trimet.TrimetClientImpl
import com.merricklabs.portlandbus.handlers.CancelandStopIntentHandler
import com.merricklabs.portlandbus.handlers.FallbackIntentHandler
import com.merricklabs.portlandbus.handlers.GetArrivalsIntentHandler
import com.merricklabs.portlandbus.handlers.HelpIntentHandler
import com.merricklabs.portlandbus.handlers.LaunchRequestHandler
import com.merricklabs.portlandbus.handlers.MyStopIntentHandler
import com.merricklabs.portlandbus.handlers.SaveStopIntentHandler
import com.merricklabs.portlandbus.handlers.SessionEndedRequestHandler
import com.merricklabs.portlandbus.storage.MyStopStorage
import com.merricklabs.portlandbus.storage.MyStopStorageImpl
import com.merricklabs.portlandbus.util.SkillsHelper
import org.koin.dsl.module.module

val PortlandBusModule = module {
    single { PortlandBusConfig() }
    single { SkillsHelper() }

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