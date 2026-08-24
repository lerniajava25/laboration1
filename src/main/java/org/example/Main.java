package org.example;
import java.util.*;

public class Main {
    static void main() {
        Scanner scanner = new Scanner(System.in);

        IO.println("Välj nå!");
        boolean active = true;

        while (active) {
            int val = scanner.nextInt();

            switch (val) {
                case 1 -> IO.println("Hello world!");
                case 2 -> IO.println("Hello Pite!");
                case 3 -> IO.println("Hello world!");
                case 4 -> active = false;
                default -> IO.println("Ogiltigt val");
            };
        }
    }
}
