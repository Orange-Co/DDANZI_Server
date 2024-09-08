package co.orange.ddanzi.domain.user.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FcmCase {

    // 판매자
    A1("💛등록하신 상품이 판매됐어요!", "24시간내에 정보입력 및 판매확정을 해주세요."),
    A2("거래가 취소됐습니다.", "24시간 동안 판매확정을 하지않아 거래가 취소됐어요."),
    A3("💛등록하신 상품의 구매가 확정됐어요!", "24시간 안에 입력하신 주소로 입금됩니다."),
    A4("진행중인 거래에 문제가 생겼습니다.", "상담센터에서 톡 상담을 진행해주세요."),

    //구매자
    B1("구매하신 상품의 판매가 취소됐어요.", "24시간 안에 결제하신 계좌로 환불됩니다. "),
    B2("💛상품 배송이 시작됐어요!","배송을 받으면 반드시 구매확정을 해주세요."),
    B3("상품이 제대로 도착하지 않았나요?","긴 시간동안 구매확정이 되지 않았어요. "),
    B4("상품 배송에 문제가 생겼나요?","24시간 후에 자동 구매확정 됩니다.")
    ;

    private final String title;
    private final String body;

    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
}
