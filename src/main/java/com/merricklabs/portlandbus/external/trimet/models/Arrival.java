package com.merricklabs.portlandbus.external.trimet.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival {

    private Date scheduled;
    private Date estimated;
    private String shortSign;
    private int locid;
    private LocalDateTime time;

    public int getStopId(){
        return locid;
    }

    public long getMinutesRemaining(LocalDateTime now){
        return now.until(this.getTime(), MINUTES);
    }

    public int getBusId(){
        // This field looks like "20 to Gresham"
        return Integer.parseInt(Arrays.asList(shortSign.split(" ")).get(0));
    }

    /**
     * Returns the estimated time, if present. Otherwise, returns the scheduled time.
     * Essentially, this returns the best guess at the arrival time.
     * @return
     */
    public LocalDateTime getTime(){
        // Lazily init time
        if (time == null) {
            time = estimated != null ? convertDate(estimated) : convertDate(scheduled);
            log.info("Set time of this arrival to {}", time);
        }

        return time;
    }

    private LocalDateTime convertDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Arrival(
            @JsonProperty("scheduled")
                    Date scheduled,
            @JsonProperty("estimated")
                    Date estimated,
            @JsonProperty("locid")
                    int locid,
            @JsonProperty("shortSign")
                    String shortSign
    ) {
        this.scheduled = scheduled;
        this.estimated = estimated;
        this.locid = locid;
        this.shortSign = shortSign;
    }
}
