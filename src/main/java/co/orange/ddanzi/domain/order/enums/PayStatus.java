package co.orange.ddanzi.domain.order.enums;

public enum PayStatus {
    PENDING("대기"),
    PAID("결제 완료"),
    FAILED("결제 실패"),
    CANCELLED("취소"),
    REFUNDED("환불")
    ;
    private String payStatus;
    PayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
