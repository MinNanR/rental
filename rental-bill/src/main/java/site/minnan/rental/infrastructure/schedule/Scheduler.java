package site.minnan.rental.infrastructure.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.minnan.rental.application.service.BillService;

@Component
@Slf4j
public class Scheduler {

    @Autowired
    private BillService billService;

    @Scheduled(cron = "0 * * * * *")
    public void billTask(){
        log.info("修改账单状态开始");
        billService.setBillUnpaid();
        log.info("修改账单状态结束");
    }
}
