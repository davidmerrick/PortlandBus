package com.merricklabs.portlandbus.external.trimet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.merricklabs.portlandbus.PortlandBusConfig;
import com.merricklabs.portlandbus.external.trimet.models.Arrival;
import com.merricklabs.portlandbus.external.trimet.models.ArrivalResults;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TrimetClientImpl implements TrimetClient {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final OkHttpClient client = new OkHttpClient();
    private final PortlandBusConfig config;

    @Inject
    public TrimetClientImpl(PortlandBusConfig config) {
        this.config = config;
    }

    @Override
    public List<Arrival> getArrivalsForStop(int stopId){
        PortlandBusConfig.Trimet trimetConfig = config.getTrimet();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(trimetConfig.getArrivalsEndpoint()).newBuilder();
        urlBuilder.addQueryParameter("locIDs", String.valueOf(stopId));
        urlBuilder.addQueryParameter("json", "true");
        urlBuilder.addQueryParameter("appID", trimetConfig.getAppId());
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            okhttp3.Response response = client.newCall(request).execute();
            ArrivalResults results = mapper.readValue(response.body().string(), ArrivalResults.class);
            log.info("Success: fetched {} arrivals from the TriMet API", results.getResultSet().getArrivals().size());
            return results.getResultSet()
                    .getArrivals()
                    .stream()
                    .filter(a -> a.getStopId() == stopId)
                    .collect(toList());
        } catch(IOException e){
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
