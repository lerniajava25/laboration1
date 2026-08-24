package org.example.api;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    static void main () throws IOException, InterruptedException {
        
        HttpClient httpClient = HttpClient
                .newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        String area = "SE3";

        String url =
                "https://www.elprisetjustnu.se/api/v1/prices/2026/08-24_" + area + ".json";

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        IO.println("Statuskod: " + response.statusCode());

        if (response.statusCode() != 200) {
            IO.println("Kunde inte hämta elpriser.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        EnergyPrice[] prices = mapper.readValue(response.body(), EnergyPrice[].class);

        IO.println("Antal timpriser: " + prices.length);

        for (EnergyPrice price : prices) {
            IO.println(price.time_start() + ": %.2f öre/kWh".formatted(price.SEK_per_kWh() * 100
            ));
        }
    }
}

record EnergyPrice(
        double SEK_per_kWh,
        double EUR_per_kWh,
        double EXR,
        String time_start,
        String time_end
) {}
