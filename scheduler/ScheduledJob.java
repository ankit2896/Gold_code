package com.freecharge.financial.scheduler;

import com.freecharge.financial.service.GoldService;
import com.freecharge.financial.serviceimpl.MockingDataImpl;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.SchedulerLock;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Profile({"qa", "staging", "prod"})
@Component
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@Slf4j
public class ScheduledJob {

    @Autowired
    private GoldService goldService;

    @Autowired
    private MockingDataImpl mockingData;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    @Value("${spring.profiles.active:qa}")
    private String env;

    @Profile("prod")
    @Scheduled(cron = "0 0 */2 * * ?")
    @SchedulerLock(name = "updateDispatchStatusForRedeemAndBuyRedeem", lockAtMostForString = "14m", lockAtLeastForString = "14m")
    public void updateDispatchStatusForRedeemAndBuyRedeem() {
        log.info("Updating dispatch statuses now : {}" , dateFormat.format(new Date()));
        goldService.updateAllDispatchStatuses();
        log.info("Dispatch Status updated successfully after : {}" ,dateFormat.format(new Date()) );
    }

    @Scheduled(cron = "0 0 */4 * * ?")
    @SchedulerLock(name = "updateSellTransactionStatus", lockAtMostForString = "5m", lockAtLeastForString = "5m")
    public void updateSellTransactionStatus() {
        log.info("Updating sell transaction statuses now : {}", dateFormat.format(new Date()));
        goldService.updateSellTransactionStatus();
        log.info("Sell transaction status updated successfully after : {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron = "0 */3 * * * ?")
    @SchedulerLock(name = "updateDispatchStatus", lockAtMostForString = "1m", lockAtLeastForString = "1m")
    public void updateDispatchStatus() {
        if(!"prod".equals(env)) {
            log.info("Mock Updating dispatch statuses now : {}", dateFormat.format(new Date()));
            mockingData.updateAllDispatchStatusesMocked();
            log.info("Mock Dispatch Status updated successfully after : {}", dateFormat.format(new Date()));
        }
    }

}
