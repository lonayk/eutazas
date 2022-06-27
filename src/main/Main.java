package main;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;


public class Main {

    private final static LocalDate SETDATE = LocalDate.of(2019, 4, 6);
    private final static String PATH = "utasadat.txt";
    private final static long WARNING_DAY = 3;

    public static void main(String[] args) {
        Route[] routes = new Route[sizeOfArray()];

        fileReading(routes);
        System.out.println();
        System.out.println("2.feladat:");
        System.out.println(numberOfBoardingPassengers(routes) + " utas szeretett volna felszállni a buszra.");

        System.out.println("***");

        System.out.println("3.feladat:");
        System.out.println("Az elutasított utasok száma: " + notValidNumberOfTickets(routes));

        System.out.println("***");

        System.out.println("4.feladat:");
        int[] busiestStation = busiestStation(routes);
        System.out.println("A legforgalmasabb állomás a " + busiestStation[0] + ". állomás "
                + busiestStation[1] + " utassal.");

        System.out.println("***");

        System.out.println("5.feladat:");
        int[] discountTicketNumbers = numberOfDiscountTickets(routes);
        System.out.println("Összesen " + discountTicketNumbers[0] + " utas utazott kedvezményes és "
                + discountTicketNumbers[1] + " ingyenes jegyel.");

        System.out.println("***");

        System.out.println("6.feladat: differenceOfDays metódus");


        System.out.println("***");

        warningForPassengers(routes);
        System.out.println("7.feladat:");
        System.out.println("A 3 napon belül lejáró bérlettel utazókat a warning.txt fájlba írtam!");


    }


    private static int sizeOfArray() {
        int arraySize = 0;
        try {
            FileReader fileReader = new FileReader(PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.readLine() != null) {
                arraySize++;
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arraySize;
    }


    private static void fileReading(Route[] routes) {
        int i = 0;
        String row;
        String[] scannedRowToArray;
        try {
            FileReader fileReader = new FileReader(PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((row = bufferedReader.readLine()) != null) {
                scannedRowToArray = row.split(" ");
                routes[i] = new Route(scannedRowToArray);
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static int numberOfBoardingPassengers(Route[] routes) {

        return routes.length;
    }


    // a következő 2 metódusnál 2 int változóval dolgoztam, szerintem ez olvashatóbb, mint a rá következő
    // numberOfDiscountTickets, ahol int tömbbel. Melyik jobb?
    private static int notValidNumberOfTickets(Route[] routes) {
        int noSingleTicket = 0;
        int expiredSeasonTicket = 0;
        for (Route route : routes) {
            if (isSeasonTicket(route)) {
                noSingleTicket += SingleTicketCheck(route);
            } else {
                expiredSeasonTicket += expirationCheck(route);
            }
        }

        return expiredSeasonTicket + noSingleTicket;
    }


    private static int[] busiestStation(Route[] routes) {
        HashMap<Integer, Integer> hashMap = getPassengersNumberByStation(routes);
        int maxPassengerNumber = 0;
        int stationNumber = 0;
        for (int i = 0; i < hashMap.size(); i++) {
            if (hashMap.get(i) > maxPassengerNumber) {
                maxPassengerNumber = hashMap.get(i);
                stationNumber = i;
            }

        }
        return new int[]{stationNumber, maxPassengerNumber};

    }


    private static int[] numberOfDiscountTickets(Route[] routes) {
        int[] numberOfDiscountTickets = new int[2];
        for (Route route : routes) {
            if (isTicketValidSeasonTicket(route)) {
                countingDiscountTickets(route, numberOfDiscountTickets);
            }
        }
        return numberOfDiscountTickets;
    }


    private static boolean isTicketValidSeasonTicket(Route route) {
        return (!route.getTicketType().equals("JGY") && (route.getValidDate().isAfter(SETDATE) ||
                route.getValidDate().equals(SETDATE)));

    }


    private static void countingDiscountTickets(Route route, int[] numberOfDiscountedTickets) {
        switch (route.getTicketType()) {
            case "TAB", "NYB" -> numberOfDiscountedTickets[0]++;
            case "NYP", "RVS", "GYK" -> numberOfDiscountedTickets[1]++;
        }
    }


    private static void warningForPassengers(Route[] routes) {

        HashMap<Integer, LocalDate> warnedPassengers = new HashMap<>();
        for (Route route : routes) {
            if (isValidDateClose(route)) {
                warnedPassengers.put(route.getCardNumber(), route.getValidDate());
            }
        }
        fileWriting(warnedPassengers);
    }


    private static boolean isValidDateClose(Route route) {
        // én így oldottam meg, nem akar tökéletesen működni, de működik.
        return (isTicketValidSeasonTicket(route) && route.getValidDate().minusDays(WARNING_DAY).isBefore(SETDATE));

        //ez egy másik megoldás akart lenni, de nem akar működni:
        //return (isTicketValidSeasonTicket(route) && (SETDATE.toEpochDay()- route.getValidDate().toEpochDay())<=WARNING_DAY);

        //Ez a 6. feladat szerinti megoldás, de valamiért ez sem akar működni:
        //return (isTicketValidSeasonTicket(route) && differenceOfDays(route)<=WARNING_DAY);
    }


    private static void fileWriting(HashMap<Integer, LocalDate> warnedPassengers) {
        try {
            FileWriter fileWriter = new FileWriter("warning.txt");
            for (Integer warnedPassenger : warnedPassengers.keySet()) {
                String key = warnedPassenger.toString();
                String value = warnedPassengers.get(warnedPassenger).toString();
                fileWriter.write(key + " " + value);
                fileWriter.write(System.lineSeparator());
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static HashMap<Integer, Integer> getPassengersNumberByStation(Route[] routes) {
        int stationNumber = routes[routes.length - 1].getStation();
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i <= stationNumber; i++) {
            hashMap.put(i, stationCounter(routes, i));
        }
        return hashMap;
    }


    private static int stationCounter(Route[] routes, int i) {
        int stationNumber = 0;
        for (Route route : routes) {
            if (route.getStation() == i) {
                stationNumber++;
            }
        }
        return stationNumber;
    }


    private static int SingleTicketCheck(Route route) {
        if (route.getTicketNumber() > 0) {
            return 0;
        }
        return 1;
    }


    private static int expirationCheck(Route routes) {
        int rejectedPassengers = 0;
        if (routes.getValidDate().toEpochDay() < SETDATE.toEpochDay()) {
            rejectedPassengers = 1;
        }
        return rejectedPassengers;
    }


    private static boolean isSeasonTicket(Route routes) {
        return routes.getTicketType().equals("JGY");
    }


    private static int differenceOfDays(Route route) {
        int routeDay = route.getDay();
        int routeMonth = route.getMonth();
        int routeYear = route.getYear();
        int setDateDay = SETDATE.getDayOfMonth();
        int setDateMonth = SETDATE.getMonthValue();
        int setDateYear = SETDATE.getYear();
        routeMonth = (routeMonth + 9) % 12;
        routeYear = routeYear - routeMonth / 10;
        routeDay = 365 * routeYear + routeYear / 4 - routeYear / 100 + routeYear / 400 + (routeMonth * 306 + 5) / 10 + routeDay - 1;
        setDateMonth = (setDateMonth + 9) % 12;
        setDateYear = setDateYear - setDateMonth / 10;
        setDateDay = 365 * setDateYear + setDateYear / 4 - setDateYear / 100 + setDateYear / 400 + (setDateMonth * 306 + 5) / 10 + setDateDay - 1;
        return setDateDay - routeDay;

    }
}