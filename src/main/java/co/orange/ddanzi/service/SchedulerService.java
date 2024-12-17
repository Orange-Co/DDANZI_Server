package co.orange.ddanzi.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class SchedulerService {

    @Autowired
    ItemService itemService;
    @Autowired
    OrderService orderService;

    /**
     *  매일 자정마다 만료된 아이템 체크.
     *  아이템 상태변경 & 재고 수 변경
    **/
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredItems() {
        log.info("Updating expired items, time : ", LocalDateTime.now());
        itemService.updateExpiredItems();
    }


    /**
     * 1분마다 실행
     * */
    @Scheduled(fixedRate = 60000)
    public void scheduleCheckOrderPlacedOrder() {
        log.info("입금 후 1일(24시간)이 지났는데, 판매확정이 되지 않은 주문 확인");
        orderService.checkOrderPlacedOrder();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckShippingOrder() {
        log.info("판매확정 후 3일 (72시간)이 지났는데, 구매확정이 되지 않은 주문 확인");
        orderService.checkShippingOrder();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckDelayedShippingOrder() {
        log.info("판매확정 후 6일 (144시간)이 지났는데, 구매확정이 되지 않았고, 신고도 하지 않은 주문 확인");
        orderService.checkDelayedShippingOrder();
    }

    @Scheduled(fixedRate = 60000)
    public void scheduleCheckWarningOrder() {
        log.info("판매확정 후 7일 (168시간)이 지났는데, 구매확정이 되지 않았고, 신고도 하지 않은 주문 확인");
        orderService.checkWarningOrder();
    }
}
