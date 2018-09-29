package com.merricklabs.portlandbus.mocks


import com.merricklabs.portlandbus.external.trimet.TrimetClient
import com.merricklabs.portlandbus.external.trimet.models.Arrival
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.stream.Collectors.toList
import java.util.stream.IntStream

class MockTrimetClient : TrimetClient, TestMock {

    var arrivals: List<Arrival> = listOf()

    override fun getArrivalsForStop(stopId: Int): List<Arrival> {
        return arrivals
    }

    override fun reset() {
        arrivals = listOf()
    }

    companion object {
        private val random = Random()

        fun getDummyArrivals(stopId: Int, numArrivals: Int): List<Arrival> {
            return IntStream.rangeClosed(1, numArrivals)
                    .mapToObj { getRandomArrival(stopId) }
                    .collect(toList())
        }

        private fun getRandomArrival(stopId: Int): Arrival {
            val localDateTime = LocalDateTime.now()
                    .plus(random.nextInt(10).toLong(), ChronoUnit.MINUTES)
            val nextArrivalTime = convertFromLocalDateTime(localDateTime)
            return Arrival(
                    nextArrivalTime,
                    nextArrivalTime,
                    stopId,
                    random.nextInt(100).toString() // random bus id
            )
        }

        private fun convertFromLocalDateTime(localDateTime: LocalDateTime): Date {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }
    }
}