package main;

import java.time.LocalDate;

public class Route {
    private int station;
    private String date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int cardNumber;
    private String ticketType;
    private int ticketNumber;
    private int validYear;
    private int validMonth;
    private int validDay;
    private LocalDate validDate;


    public Route(String[] array) {
        station = Integer.parseInt(array[0]);
        date = array[1].split("-")[0];
        String[] tmp = array[1].split("-");
        year = Integer.parseInt(tmp[0].substring(0, 4));
        month = Integer.parseInt(tmp[0].substring(4, 6));
        day = Integer.parseInt(tmp[0].substring(6));
        hour = Integer.parseInt(tmp[1].substring(0, 2));
        minute = Integer.parseInt(tmp[1].substring(3));
        cardNumber = Integer.parseInt(array[2]);
        ticketType = array[3];
        if (ticketType.equals("JGY")) {
            ticketNumber = Integer.parseInt(array[4]);
        } else {
            validYear = Integer.parseInt(array[4].substring(0, 4));
            validMonth = Integer.parseInt(array[4].substring(4, 6));
            validDay = Integer.parseInt(array[4].substring(6));
            validDate = LocalDate.of(validYear, validMonth, validDay);
        }


    }

    public int getStation() {
        return station;
    }

    public String getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public String getTicketType() {
        return ticketType;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public int getValidYear() {
        return validYear;
    }

    public int getValidMonth() {
        return validMonth;
    }

    public int getValidDay() {
        return validDay;
    }

    public LocalDate getValidDate() {
        return validDate;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Út { megálló: ");
        stringBuilder.append(station);
        stringBuilder.append(". Év: ");
        stringBuilder.append(year);
        stringBuilder.append(" hó: ");
        stringBuilder.append(month);
        stringBuilder.append(" nap: ");
        stringBuilder.append(day);
        stringBuilder.append(" óra: ");
        stringBuilder.append(hour);
        stringBuilder.append(" perc: ");
        stringBuilder.append(minute);
        stringBuilder.append(", kártyaazonosító: ");
        stringBuilder.append(cardNumber);
        stringBuilder.append(" jegytípus: ");
        stringBuilder.append(ticketType);
        stringBuilder.append(". Érvényessége: ");
        stringBuilder.append(validDate);
        stringBuilder.append(". }");
        return stringBuilder.toString();
    }
}

