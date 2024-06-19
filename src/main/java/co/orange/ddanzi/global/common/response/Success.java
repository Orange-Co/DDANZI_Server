package co.orange.ddanzi.global.common.response;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {
    // Default
    SUCCESS(HttpStatus.OK, "Request successfully processed."),

    // 200 OK SUCCESS
    GET_HOME_INFO_SUCCESS(HttpStatus.OK, "Successfully retrieved home information."),
    GET_PRODUCT_DETAIL_SUCCESS(HttpStatus.OK, "Successfully retrieved product details."),

    GET_SEARCH_SCREEN_SUCCESS(HttpStatus.OK, "Successfully retrieved the search screen."),
    GET_SEARCH_RESULTS_SUCCESS(HttpStatus.OK, "Successfully performed the search."),

    GET_MY_PAGE_INFO_SUCCESS(HttpStatus.OK, "Successfully retrieved mypage information."),
    GET_MY_ORDER_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my order list."),
    GET_MY_ITEM_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my item list."),
    GET_MY_INTEREST_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my interesting product list."),

    GET_SETTING_SCREEN_SUCCESS(HttpStatus.OK, "Successfully retrieved setting screen."),
    GET_SETTING_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully retrieved the delivery address."),
    GET_SETTING_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully retrieved the account."),

    PUT_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully updated the delivery address."),
    PUT_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully updated the account."),
    PUT_PUSH_ALARM_SUCCESS(HttpStatus.OK, "Successfully updated the push alarm status."),

    DELETE_INTEREST_SUCCESS(HttpStatus.OK, "Successfully removed the product from interest List."),
    DELETE_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully deleted the delivery address."),
    DELETE_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully deleted the account"),


    // 201 CREATED
    CREATE_INTEREST_SUCCESS(HttpStatus.CREATED, "Successfully added the product to interest List."),
    CREATE_ADDRESS_SUCCESS(HttpStatus.CREATED, "Successfully added the delivery address."),
    CREATE_ACCOUNT_SUCCESS(HttpStatus.CREATED, "Successfully added the account."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
