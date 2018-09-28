package com.merricklabs.portlandbus.external.trimet.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit.MINUTES
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Arrival(
        @param:JsonProperty("scheduled")
        private val scheduled: Date,
        @param:JsonProperty("estimated")
        private val estimated: Date?,
        @param:JsonProperty("locid")
        val stopId: Int,
        @param:JsonProperty("shortSign")
        private val shortSign: String
) {

    /**
     * Returns the estimated time, if present. Otherwise, returns the scheduled time.
     * Essentially, this returns the best guess at the arrival time.
     * @return
     */
    private var _time: LocalDateTime? = null
    val time: LocalDateTime
        get() {
            // Lazily init time
            if (_time == null) {
                _time = if (estimated != null) convertDate(estimated) else convertDate(scheduled)
                // log.debug("Set time of this arrival to {}", time)
            }
            return _time ?: throw RuntimeException("Time should not be null.")
        }

    // This field looks like "20 to Gresham"
    val busId: Int
        get() =
            Integer.parseInt(Arrays.asList(*shortSign.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())[0])

    fun getMinutesRemaining(now: LocalDateTime): Long {
        return now.until(this.time, MINUTES)
    }

    private fun convertDate(date: Date): LocalDateTime {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }
}
