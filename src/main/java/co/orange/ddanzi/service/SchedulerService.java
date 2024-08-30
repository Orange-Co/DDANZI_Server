package co.orange.ddanzi.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {

    @Autowired
    ItemService itemService;

    /**
     *  매일 자정마다 만료된 아이템 체크.
     *  아이템 상태변경 & 재고 수 변경
    **/
    @Transactional
    @Scheduled(cron = "0 50 0 * * ?")
    public void updateExpiredItems() {
        log.info("Updating expired items");
        itemService.updateExpiredItems();
    }
}
