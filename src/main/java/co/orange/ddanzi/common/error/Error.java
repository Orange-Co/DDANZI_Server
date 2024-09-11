package co.orange.ddanzi.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum Error {
    // Default
    ERROR(HttpStatus.BAD_REQUEST, "Request processing failed"),

    // 400 BAD REQUEST
    REFRESH_TOKEN_IS_NULL(HttpStatus.BAD_REQUEST, "Refresh token is null"),
    ACCOUNT_NAME_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "The account name does not match to user name."),
    DUE_DATE_IS_INCORRECT(HttpStatus.BAD_REQUEST, "The due date is incorrect."),
    ITEM_IS_NOT_ON_SALE(HttpStatus.BAD_REQUEST, "The item is not on sale."),
    INVALID_ORDER_STATUS(HttpStatus.BAD_REQUEST,"잘못된 주문 상태입니다."),

    //402 Payment Required
    PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "Payment is required to place the order."),

    // 403 UNAUTHORIZED
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"Unauthorized user"),
    ITEM_UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED,"Access denied. This item is not owned by you."),
    INVALID_JWT_EXCEPTION(HttpStatus.UNAUTHORIZED, "Invalid JWT"),
    LOG_OUT_JWT_TOKEN(HttpStatus.UNAUTHORIZED,"Logged out user"),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"JWT expired"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,"Refresh Token expired"),
    JWT_TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED,"JWT value does not exist in header"),


    // 404 NOT FOUND
    AUTHENTICATION_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "The Authentication of user does not exist."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User does not exist."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "The product does not exist."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "The item does not exist."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "The order does not exist."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "The payment does not exist."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "The category of the product does not exist."),
    DISCOUNT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "The discount info of the category does not exist."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "The address does not exist."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "The account does not exist."),
    PUSH_ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "The push alarm of user does not exist."),
    TERM_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for sign up does not exist."),
    TERM_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for order does not exist."),
    TERM_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for registering item does not exist."),

    // 405 METHOD_NOT_ALLOWED

    // 422 UNPROCESSABLE ENTITY
    NO_ITEM_ON_SALE(HttpStatus.UNPROCESSABLE_ENTITY, "현재 재고가 없어 환불 처리가 진행되었습니다."),

    // 409 CONFLICT,
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "The account already exists."),
    AUTHENTICATION_ALREADY_EXISTS(HttpStatus.CONFLICT, "The identity authentication of user already exists."),
    PAYMENT_CANNOT_CHANGE(HttpStatus.CONFLICT, "The payment status cannot be changed."),
    ORDER_STATUS_CANNOT_CHANGE(HttpStatus.CONFLICT, "The order status cannot be changed."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    REFUND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "재고가 없어 환불처리를 진행하였으나 환불에 실패하였습니다. 고객센터에 문의바랍니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
