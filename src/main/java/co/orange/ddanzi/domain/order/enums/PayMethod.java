package co.orange.ddanzi.domain.order.enums;

public enum PayMethod {
    card("신용/체크카드"),
    naverpay_card("네이버페이"),
    kakaopay("카카오페이"),
    samsungpay("삼성페이"),
    // trans("실시간 계좌이체"),
    phone("휴대폰 결제")
    ;

    private String payMethod;

    PayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getDescription() {
        return payMethod;
    }
}
