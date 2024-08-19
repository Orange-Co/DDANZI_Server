package co.orange.ddanzi.domain.order.enums;

public enum PayMethod {
    CARD("신용/체크카드"),
    NAVERPAY("네이버페이"),
    KAKAOPAY("카카오페이"),
    SAMSUNGPAY("삼성페이"),
    ACCOUNT("계좌이체"),
    PHONE("휴대폰 결제")
    ;

    private String payMethod;
    PayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
