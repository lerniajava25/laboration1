package org.example;

import java.io.IOException;
import java.util.*;

import org.example.api.ApiClient;
import org.example.api.ApiClient.EnergyPrice;
import org.example.PriceAnalyzer.PriceAnalyzer;

public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        ApiClient apiClient = new ApiClient();
        EnergyPrice[] prices = null;
        boolean active = true;
        var selectedArea = "";

        PriceAnalyzer priceAnalyzer = new PriceAnalyzer();

        while (active) {
            IO.println("""
                    
                    Elpriser – Analysverktyg
                    ========================
                    1. Välj elområde (SE1, SE2, SE3, SE4)
                    2. Min, Max och Medelpris
                    3. Sortera priser (lägst till högst)
                    4. Bästa laddningstid (4h sammanhängande)
                    e. Avsluta
                    """);

            IO.println("Välj ett alternativ:");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "1" -> {
                    IO.println("Välj elområde: SE1, SE2, SE3 eller SE4");
                    String area = scanner.nextLine().trim().toUpperCase();

                    switch (area) {
                        case "SE1", "SE2", "SE3", "SE4" -> {
                            selectedArea = area;

                            try {
                                prices = apiClient.getPrices(selectedArea);

                                IO.println("Valt elområde: " + selectedArea);
                                IO.println("Hämtade " + prices.length + " priser.");

                            } catch (IOException | InterruptedException e) {
                                Thread.currentThread().interrupt();
                                IO.println("Kunde inte hämta priser: " + e.getMessage());
                            }
                        }

                        default -> IO.println("Ogiltigt val");
                    }
                }

                case "2" -> {
                    IO.println("Här ska min-, max- och medelpris visas för "
                            + selectedArea + ".");
                    assert prices != null;
                    priceAnalyzer.showMinMaxAverage(prices);
                }

                case "3" -> {
                    IO.println("Här visas sorterade el-priser.");
                    priceAnalyzer.showSortedPrices(prices);
                }

                case "4" -> {
                    IO.println("Här visade de billigaste laddningstimmarna i ett intervall av  4-timmarsperiod.");
                    priceAnalyzer.showBestChargingTime(prices);
                }

                case "e" -> {
                    active = false;
                    IO.println("Programmet avslutas.");
                }

                default -> IO.println("Ogiltigt val");
            }
        }
    }
}