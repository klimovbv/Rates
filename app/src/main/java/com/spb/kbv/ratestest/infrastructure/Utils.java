package com.spb.kbv.ratestest.infrastructure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static final class DateFormat {

        private static SimpleDateFormat dateFormat;

        public static String listFormat(Date date){
            dateFormat = new SimpleDateFormat("E dd");
            return dateFormat.format(date);
        }

        public static String infoFormat (String dateString) {
            dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return newDateFormat.format(date);
        }

        public static String queryFormatString (Date date) {
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.format(date);
        }

        public static String addedCentralFormat (Date date) {
            dateFormat = new SimpleDateFormat("yyyy MMM E dd");
            return dateFormat.format(date);
        }

        public static String addedCentralYearFormat (Date date) {
            dateFormat = new SimpleDateFormat("yyyy");
            return dateFormat.format(date);
        }

        public static String addedMonthFormat (Date date) {
            dateFormat = new SimpleDateFormat("MMM");
            return dateFormat.format(date);
        }
    }
}
