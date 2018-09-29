package com.merricklabs.portlandbus.external.trimet

import com.fasterxml.jackson.databind.ObjectMapper
import com.merricklabs.portlandbus.PortlandBusConfig
import com.merricklabs.portlandbus.external.trimet.models.Arrival
import com.merricklabs.portlandbus.external.trimet.models.ArrivalResults
import mu.KotlinLogging
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.io.IOException

private val log = KotlinLogging.logger {}

class TrimetClientImpl : TrimetClient, KoinComponent {

    val config by inject<PortlandBusConfig>()

    override fun getArrivalsForStop(stopId: Int): List<Arrival> {
        val url = HttpUrl.parse(config.trimet.arrivalsEndpoint)!!.newBuilder()
                .addQueryParameter("locIDs", stopId.toString())
                .addQueryParameter("json", "true")
                .addQueryParameter("appID", config.trimet.appId)
                .build()
                .toString()

        val request = Request.Builder()
                .url(url)
                .build()
        try {
            val response = client.newCall(request).execute()
            val results = mapper.readValue(response.body()!!.string(), ArrivalResults::class.java)
            log.info { "Success: fetched ${results.resultSet.arrivals.size} arrivals from the TriMet API" }
            return results.resultSet
                    .arrivals
                    .asSequence()
                    .filter { it.stopId == stopId }
                    .toList()
        } catch (e: IOException) {
            log.error(e.message, e)
            throw RuntimeException(e)
        }
    }

    companion object {
        private val mapper = ObjectMapper()
        private val client = OkHttpClient()
    }
}
