package co.orange.ddanzi.global.common.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {
    // Default
    ERROR(HttpStatus.BAD_REQUEST, "Request processing failed"),

    // 400 BAD REQUEST
    ACCOUNT_NAME_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "The account name does not match to user name."),
    DUE_DATE_IS_INCORRECT(HttpStatus.BAD_REQUEST, "The due date is incorrect."),

    // 401 UNAUTHORIZED


    // 403 Forbidden


    // 404 NOT FOUND
    AUTHENTICATION_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "The Authentication of user does not exist."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "The product does not exist."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "The category of the product does not exist."),
    DISCOUNT_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "The discount info of the category does not exist."),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "The address does not exist."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "The account does not exist."),
    PUSH_ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "The push alarm of user does not exist."),
    TERM_JOIN_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for sign up does not exist."),
    TERM_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for order does not exist."),
    TERM_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "The term for registering item does not exist."),

    // 405 METHOD_NOT_ALLOWED


    // 409 CONFLICT,
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "The account already exists."),



    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
