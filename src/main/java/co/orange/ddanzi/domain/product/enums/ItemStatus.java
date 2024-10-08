package co.orange.ddanzi.domain.product.enums;

import lombok.Getter;

@Getter
public enum ItemStatus {
    ON_SALE("판매중"),
    IN_TRANSACTION("거래중"),
    EXPIRED("만료됨"),
    CLOSED("거래완료"),
    DELETED("삭제됨")
    ;

    private final String itemStatus;
    ItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }
}
