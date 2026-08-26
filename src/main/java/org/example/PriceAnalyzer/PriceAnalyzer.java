package org.example.PriceAnalyzer;

import org.example.api.ApiClient;
import org.example.api.ApiClient.EnergyPrice;
import java.util.*;

public class PriceAnalyzer {

    /*
    Visar min, max och medel- priserna.
     */
    public void showMinMaxAverage(ApiClient.EnergyPrice[] prices) {

        double minPrice = prices[0].SEK_per_kWh() * 100;
        double maxPrice = prices[0].SEK_per_kWh() * 100;
        double total = 0;

        for (ApiClient.EnergyPrice price : prices) {
            double priceInOre = price.SEK_per_kWh() * 100;

            if (priceInOre < minPrice) {
                minPrice = priceInOre;
            }

            if (priceInOre > maxPrice) {
                maxPrice = priceInOre;
            }

            total = total + priceInOre;
        }

        double averagePrice = total / prices.length;

        IO.println("Lägsta pris: %.2f öre/kWh".formatted(minPrice));
        IO.println("Högsta pris: %.2f öre/kWh".formatted(maxPrice));
        IO.println("Medelpris: %.2f öre/kWh".formatted(averagePrice));
    }

    /*
    Sorterar priserna
     */
    public void showSortedPrices(EnergyPrice[] prices) {

        if (prices == null || prices.length == 0) {
            IO.println("Det finns inga priser att sortera.");
            return;
        }

        EnergyPrice[] sortedPrices = Arrays.copyOf(
                prices,
                prices.length
        );

        Arrays.sort(
                sortedPrices,
                Comparator.comparingDouble(EnergyPrice::SEK_per_kWh) // Ska vara så?
        );

        for (EnergyPrice price : sortedPrices) {
            IO.println(
                    price.time_start() + ": %.2f öre/kWh".formatted(
                            price.SEK_per_kWh() * 100
                    )
            );
        }
    }

    /*
    Visar den billigaste laddningstiden med ett intervall på 4 timmar.
     */
    public void showBestChargingTime(EnergyPrice[] prices) {

        int intervalsInFourHours = 16;

        if (prices == null || prices.length < intervalsInFourHours) {
            IO.println("Det finns inte tillräckligt med prisdata.");
            return;
        }

        double currentTotal = 0;

        for (int i = 0; i < intervalsInFourHours; i++) {
            currentTotal += prices[i].SEK_per_kWh() * 100;
        }

        double lowestTotal = currentTotal;
        int bestStartIndex = 0;

        for (int startIndex = 1;
             startIndex <= prices.length - intervalsInFourHours;
             startIndex++) {

            currentTotal -= prices[startIndex - 1].SEK_per_kWh() * 100;

            currentTotal += prices[
                    startIndex + intervalsInFourHours - 1
                    ].SEK_per_kWh() * 100;

            if (currentTotal < lowestTotal) {
                lowestTotal = currentTotal;
                bestStartIndex = startIndex;
            }
        }

        EnergyPrice startPrice = prices[bestStartIndex];

        EnergyPrice endPrice = prices[bestStartIndex + intervalsInFourHours - 1];

        double averagePrice = lowestTotal / intervalsInFourHours;

        IO.println("Bästa laddningstid:");
        IO.println(startPrice.time_start()
                + " till "
                + endPrice.time_end());

        IO.println(
                "Medelpris: %.2f öre/kWh".formatted(averagePrice)
        );
    }
}