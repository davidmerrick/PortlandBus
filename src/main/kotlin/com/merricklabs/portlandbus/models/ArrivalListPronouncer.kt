package com.merricklabs.portlandbus.models

import com.merricklabs.portlandbus.external.trimet.models.Arrival
import java.time.LocalDateTime

class ArrivalListPronouncer(private val stopId: Int, arrivals: List<Arrival>) {

    private val arrivalPronouncers: List<ArrivalPronouncer>

    init {
        // Filter out arrivals that are in the past and below a time threshold
        this.arrivalPronouncers = arrivals
                .asSequence()
                .filter { it.time.isAfter(now) }
                .filter { it.getMinutesRemaining(now) < MAX_ARRIVAL_MINUTES }
                .map { ArrivalPronouncer(it) }
                .toList()
    }

    private fun pronounceStop(): String {
        return pronounceStop(stopId)
    }

    fun pronounceArrivals(): String {
        val now = LocalDateTime.now()

        if (arrivalPronouncers.isEmpty()) {
            return "No arrivals are currently scheduled for stop ${pronounceStop()} in the next $MAX_ARRIVAL_MINUTES minutes."
        }

        val speechBuilder = StringBuilder("Next arrivals at stop ${pronounceStop()}: ")

        if (arrivalPronouncers.size == 1) {
            speechBuilder.append(arrivalPronouncers[0].pronounceForMultipleArrival(now))
        } else {
            arrivalPronouncers.subList(0, arrivalPronouncers.size - 1)
                    .forEach { speechBuilder.append("${it.pronounceForMultipleArrival(now)}, ") }

            speechBuilder.append("and ${arrivalPronouncers[arrivalPronouncers.size - 1].pronounceForMultipleArrival(now)}.")
        }
        return speechBuilder.toString()
    }

    /**
     * Format arrivals for display on Echo Show
     * @return
     */
    fun showArrivals(): String {
        val textBuilder = StringBuilder("Stop $stopId: \n")

        if (arrivalPronouncers.isEmpty()) {
            textBuilder.append("No arrivals")
        } else {
            arrivalPronouncers
                    .stream()
                    .forEach { textBuilder.append("${it.showNextArrival(now)}\n") }
        }
        return textBuilder.toString()
    }

    companion object {
        private val now = LocalDateTime.now()
        private val MAX_ARRIVAL_MINUTES = 30

        fun pronounceStop(stopId: Int): String {
            return if (stopId <= 1000) {
                stopId.toString()
            } else {
                "<say-as interpret-as=\"digits\">$stopId</say-as>"
            }
        }
    }
}
