package com.freecharge.financial.utility;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.WordUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class StringUtil {


    public static String capitalize(String value) {
        String camelCaseValue = WordUtils.capitalizeFully(value);
        return camelCaseValue.trim();
    }

    public static String convertToDDMonthYY(String userDate) {
        String strDate = userDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(userDate);
            DateFormat out = new SimpleDateFormat("dd MMM yy");
            strDate = out.format(date);
        } catch (Exception ex) {
            log.error("Exception :: " + ex);
        }
        return strDate;
    }

    public static String convertYYMMDDToDDMonthYY(String userDate) {
        String strDate = userDate;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(userDate);
            DateFormat out = new SimpleDateFormat("dd MMM yy");
            strDate = out.format(date);
        } catch (Exception ex) {
            log.error("Exception :: " + ex);
        }
        return strDate;
    }

    public static String convertFromEnglishDateToYYYYMonthDD(String userDate)
    {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
        try {
            date = formatter.parse(userDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return formattedDate;
    }
}
