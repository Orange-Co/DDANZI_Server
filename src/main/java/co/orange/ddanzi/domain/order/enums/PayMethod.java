package co.orange.ddanzi.domain.order.enums;

public enum PayMethod {
    CARD("카드"),
    NAVERPAY("네이버페이")
    ;

    private String payMethod;
    PayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
