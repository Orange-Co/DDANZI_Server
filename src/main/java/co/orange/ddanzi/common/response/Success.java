package co.orange.ddanzi.common.response;


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
    GET_REDIS_KEY_SUCCESS(HttpStatus.CREATED, "Successfully retrieved the redis key"),
    GET_GCP_SIGNED_URL_SUCCESS(HttpStatus.OK, "Successfully retrieved the GCP signed URL"),

    GET_HOME_INFO_SUCCESS(HttpStatus.OK, "Successfully retrieved home information."),
    GET_PRODUCT_DETAIL_SUCCESS(HttpStatus.OK, "Successfully retrieved product details."),

    GET_ALARM_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved user's alarm list."),

    GET_SEARCH_SCREEN_SUCCESS(HttpStatus.OK, "Successfully retrieved the search screen."),
    GET_SEARCH_RESULTS_SUCCESS(HttpStatus.OK, "Successfully performed the search."),

    GET_ORDER_PRODUCT_SUCCESS(HttpStatus.OK, "Successfully retrieved product for order information."),
    GET_ORDER_DETAIL_SUCCESS(HttpStatus.OK, "Successfully retrieved order details."),

    GET_MOST_SIMILAR_PRODUCT_SUCCESS(HttpStatus.OK, "Successfully retrieved most similar product."),
    GET_ITEM_PRODUCT_SUCCESS(HttpStatus.OK, "Successfully retrieved product to register item."),
    GET_ITEM_DETAIL_SUCCESS(HttpStatus.OK, "Successfully retrieved item details."),
    GET_ORDER_ADDRESS_OPTION_SUCCESS(HttpStatus.OK, "Successfully retrieved the address and option of the order."),

    GET_MY_PAGE_INFO_SUCCESS(HttpStatus.OK, "Successfully retrieved mypage information."),
    GET_MY_ORDER_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my order list."),
    GET_MY_ITEM_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my item list."),
    GET_MY_INTEREST_LIST_SUCCESS(HttpStatus.OK, "Successfully retrieved my interesting product list."),

    GET_SETTING_SCREEN_SUCCESS(HttpStatus.OK, "Successfully retrieved setting screen."),
    GET_SETTING_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully retrieved the delivery address."),
    GET_SETTING_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully retrieved the account."),

    PATCH_ORDER_STATUS_SUCCESS(HttpStatus.OK, "Successfully updated the order status."),
    PATCH_PAYMENT_STATUS_SUCCESS(HttpStatus.OK, "Successfully updated payment status."),
    PATCH_ALARM_ISCHECKED_SUCCESS(HttpStatus.OK, "Successfully updated the alarm's checked status."),

    PUT_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully updated the delivery address."),
    PUT_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully updated the account."),
    PUT_PUSH_ALARM_SUCCESS(HttpStatus.OK, "Successfully updated the push alarm status."),

    DELETE_USER_SUCCESS(HttpStatus.OK, "Successfully deleted the user."),
    DELETE_INTEREST_SUCCESS(HttpStatus.OK, "Successfully removed the product from interest List."),
    DELETE_ADDRESS_SUCCESS(HttpStatus.OK, "Successfully deleted the delivery address."),
    DELETE_ACCOUNT_SUCCESS(HttpStatus.OK, "Successfully deleted the account."),
    DELETE_ITEM_SUCCESS(HttpStatus.OK, "Successfully deleted the item."),

    // 201 CREATED
    SIGNIN_KAKAO_SUCCESS(HttpStatus.CREATED, "Successfully sign in using Kakao"),
    SIGNIN_APPLE_SUCCESS(HttpStatus.CREATED, "Successfully sign in using Kakao"),

    REFRESH_ACCESS_TOKEN_SUCCESS(HttpStatus.CREATED, "Successfully refreshed access token."),
    LOGOUT_SUCCESS(HttpStatus.CREATED, "Successfully log out the user"),

    SET_REDIS_KEY_SUCCESS(HttpStatus.CREATED, "Successfully set the redis key"),


    CREATE_AUTHENTICATION_SUCCESS(HttpStatus.CREATED, "Successfully verified identity."),
    CREATE_PRODUCT_SUCCESS(HttpStatus.CREATED, "Successfully confirmed the product."),
    CREATE_PAYMENT_SUCCESS(HttpStatus.CREATED, "Successfully registered payment information."),
    CREATE_ORDER_SUCCESS(HttpStatus.CREATED, "Successfully completed the order."),
    CREATE_ITEM_SUCCESS(HttpStatus.CREATED, "Successfully listed the item for sale."),
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
