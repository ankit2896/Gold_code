package com.freecharge.financial.utility;


import com.freecharge.financial.constants.GoldConstant;
import com.freecharge.financial.dto.enums.ResponseCodes;
import com.freecharge.financial.dto.enums.TransactionType;
import com.freecharge.financial.dto.request.VeriyPriceRequest;
import com.freecharge.financial.exception.GoldServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GoldUtils {

    private static final Logger logger = LoggerFactory.getLogger(GoldUtils.class);

    public static Integer getMinimumBuyPrice(HttpServletRequest httpServletRequest) {

        String fcChannel = httpServletRequest.getParameter("fcChannel");
        String fcVersion = httpServletRequest.getParameter("fcversion");
        String goalId = httpServletRequest.getParameter(GoldConstant.GOAL_ID);

        logger.info("FcChannel:{} , FcVersion:{}  and goalId:{}" , fcChannel , fcVersion,goalId);

        if(GoldConstant.CHANNEL_ANDROID.equalsIgnoreCase(fcChannel)) {

            Integer int_fcversion = -1;
            try {
                int_fcversion = Integer.parseInt(fcVersion);
            } catch (Exception e) {
                // for catching exception
                logger.error("Exception parsing fcVersion: ", e);
            }

            if(int_fcversion < GoldConstant.ANDROID_VERSION_SUPPORTED) {
                return GoldConstant.MIN_AMOUNT_PER_TRANSACTION;
            } else {
                return GoldConstant.MIN_AMOUNT_PER_TRANSACTION_V2;
            }

        }

        return GoldConstant.MIN_AMOUNT_PER_TRANSACTION_V2;

    }

    public static void validateBlock(VeriyPriceRequest priceRequest, HttpServletRequest httpServletRequest) throws GoldServiceException {

        if (priceRequest.getTransactionType().equals(TransactionType.SELL)
                || priceRequest.getTransactionType().equals(TransactionType.REDEEM_GOLD)
                || priceRequest.getTransactionType().equals(TransactionType.BUY_REDEEM_GOLD)) {
            return;
        }

        Integer minimumBuyPrice = getMinimumBuyPrice(httpServletRequest);

        if (priceRequest.getGoldPrice().compareTo(new BigDecimal(minimumBuyPrice)) < 0) {
            if(minimumBuyPrice == GoldConstant.MIN_AMOUNT_PER_TRANSACTION) {
                throw new GoldServiceException(ResponseCodes.MIN_BUY_AMOUNT.getCode(), ResponseCodes.MIN_BUY_AMOUNT.getMessage());

            } else {
                throw new GoldServiceException(ResponseCodes.MIN_BUY_AMOOUNT_V2.getCode(), ResponseCodes.MIN_BUY_AMOOUNT_V2.getMessage());

            }
        }

    }

    public static Map<String, String> getRequestMetdata(String url, String queryString, String body) {
        Map<String, String> map = new HashMap<>();
        map.put(GoldConstant.URL, url);
        map.put(GoldConstant.QUERY_PARAMS, queryString);
        map.put(GoldConstant.REQUEST_BODY, body);
        return map;
    }

    public static  boolean hasNext(String str){
        return StringUtils.hasText(str) && !str.equalsIgnoreCase("null");
    }

    public static String getValueFromMapAsString(Map<Object, Object> map, String keyName) {
        return GoldUtils.hasNext(String.valueOf(map.get(keyName))) ? String.valueOf(map.get(keyName)) : null;
    }


    private static Period getPeriodBetween(String fromDate, String toDate) {
        LocalDate startDate = LocalDate.parse(fromDate);
        LocalDate endDate = LocalDate.parse(toDate);
        Period period = Period.between(startDate, endDate);
        return period;
    }

    public static float getTimePeriodInYears(String fromDate, String toDate) {
        Period period = getPeriodBetween(fromDate , toDate);
        int years = Math.abs(period.getYears());
        float months = Math.abs(period.getMonths());
        months = months/12;
        return years+months;
    }

    public static int getTimePeriodInMonths(String fromDate , String toDate)
    {
        Period period = getPeriodBetween(fromDate , toDate);
        int years = Math.abs(period.getYears());
        int months = Math.abs(period.getMonths());
        int totalMonths = (years*12)+months;
        return totalMonths;
    }

    public static String getDateFromEpoch(long epoch) {
       Date date = new Date(epoch*1000);
       String targetDate = StringUtil.convertFromEnglishDateToYYYYMonthDD(String.valueOf(date));
        return targetDate;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        logger.info("value:{} , places:{}" , value , places);
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public static String getCurrentDateAndTime() {
       SimpleDateFormat dateFormat = new SimpleDateFormat(GoldConstant.DATE_FORMAT);
        return dateFormat.format(new Date());
       }

}
