package com.merricklabs.portlandbus.testutil


import com.merricklabs.portlandbus.external.trimet.models.Arrival
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Random

object MockTrimetUtils {
    private val random = Random()

    fun getDummyArrivals(stopId: Int, numArrivals: Int): List<Arrival> {
        return generateSequence(0) { it + 1 }
                .takeWhile { it < numArrivals }
                .map { getRandomArrival(stopId) }
                .toList()
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