package com.freecharge.financial.scheduler;

import com.freecharge.financial.cache.IGoldRedisCacheService;
import com.freecharge.financial.constants.GoldCacheConstants;
import com.freecharge.financial.producer.UPMActivity;
import com.freecharge.financial.dto.request.UpdateMobileNumberDto;
import com.freecharge.financial.producer.GoldPurchaseProducer;
import com.freecharge.financial.service.GoldService;
import com.freecharge.financial.service.IUtilityService;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@Slf4j
public class GoldScheduler {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    @Autowired
    private GoldService goldService;

    @Autowired
    private UPMActivity upmActivityForExistingUser;

    @Autowired
    private IUtilityService iUtilityService;

    @Autowired
    IGoldRedisCacheService goldRedisCacheService;


    //Every 6:00 AM
    @Scheduled(cron = "0 0 6 * * ?")
    @SchedulerLock(name = "scheduleTaskWithCronExpression", lockAtMostForString = "5m", lockAtLeastForString = "5m")
    public void scheduleTaskWithCronExpression() {
        log.info("GoldScheduler time is now {}", dateFormat.format(new Date()));
        goldService.flushRedeemProductDetails();
       // goldRedisCacheService.flushProductDetails(GoldCacheConstants.GOLD_COIN_DETAIL);
        log.info("GoldScheduler time is end {}", dateFormat.format(new Date()));
    }

   /* // initialDelay - 5 min
    // fixedDelay - 6 min
    @Scheduled(initialDelay = 300000, fixedDelay = 360000)

    public void scheduleForUpmEvent() {
        log.info("GoldPurchaseProducer event time is now {}", dateFormat.format(new Date()));
        upmActivityForExistingUser.upmTriggerEventForExistingUserOnce();
        log.info("GoldPurchaseProducer event time is end {}", dateFormat.format(new Date()));
    }*/

    @Scheduled(cron = "0 0 6 * * MON")
    @SchedulerLock(name = "generateReportErrorInUpdateMobileNumber", lockAtMostForString = "5m", lockAtLeastForString = "5m")
    public void generateReportErrorInUpdateMobileNumber() {
        log.info("Generate report error in update mobile number now {}", dateFormat.format(new Date()));
        Set<UpdateMobileNumberDto> errorUpdateMobileNumberList = goldService.getErrorInUpdateMobileNumbers();
        log.info("Update mobile number report ->{}", errorUpdateMobileNumberList);
        iUtilityService.sendEmailAlerts("Failure update mobile number report",
                errorUpdateMobileNumberList.toString());
        log.info("Generate report error in update mobile number time is end {}", dateFormat.format(new Date()));
    }


}

