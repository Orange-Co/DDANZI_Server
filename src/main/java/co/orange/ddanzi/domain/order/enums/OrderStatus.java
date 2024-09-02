package co.orange.ddanzi.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    ORDER_PENDING("주문 대기"),
    ORDER_PLACE("입금 완료"),
    SHIPPING("배송 중"),
    COMPLETED("거래 완료"),
    CANCELLED("거래 취소")
    ;
    private final String orderStatus;
    OrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
