package com.merricklabs.portlandbus.mocks;

import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import com.merricklabs.portlandbus.external.trimet.TrimetClient;
import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import com.sun.org.glassfish.external.statistics.annotations.Reset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.Setter;

import static java.util.stream.Collectors.toList;

@Singleton
public class MockTrimetClient implements TrimetClient, TestMock {

    private static final Random random = new Random();

    @Setter
    private List<Arrival> arrivals = ImmutableList.of();

    @Override
    public List<Arrival> getArrivalsForStop(int stopId) {
        return arrivals;
    }

    @Reset
    public void reset(){
        arrivals = ImmutableList.of();
    }

    public static List<Arrival> getDummyArrivals(final int stopId, int numArrivals) {
        return IntStream.rangeClosed(1, numArrivals)
                .mapToObj(i -> getRandomArrival(stopId))
                .collect(toList());
    }

    private static Arrival getRandomArrival(int stopId) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .plus(random.nextInt(10), ChronoUnit.MINUTES);
        Date nextArrivalTime = convertFromLocalDateTime(localDateTime);
        return new Arrival(
                nextArrivalTime,
                nextArrivalTime,
                stopId,
                String.valueOf(random.nextInt(100)) // random bus id
        );
    }

    private static Date convertFromLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
