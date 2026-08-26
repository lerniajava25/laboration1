package org.example.api;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class ApiClient {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public EnergyPrice[] getPrices(String selectedArea)
            throws IOException, InterruptedException {

        LocalDate today = LocalDate.now();

        String url =
                "https://www.elprisetjustnu.se/api/v1/prices/%d/%02d-%02d_%s.json"
                        .formatted(
                                today.getYear(),
                                today.getMonthValue(),
                                today.getDayOfMonth(),
                                selectedArea
                        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Kunde inte hämta elpriser. Statuskod: "
                            + response.statusCode()
            );
        }

        return mapper.readValue(
                response.body(),
                EnergyPrice[].class
        );
    }

    public record EnergyPrice(
            double SEK_per_kWh,
            double EUR_per_kWh,
            double EXR,
            String time_start,
            String time_end
    ) {
    }
}