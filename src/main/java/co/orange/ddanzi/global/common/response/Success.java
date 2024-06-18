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

    DELETE_INTEREST_SUCCESS(HttpStatus.OK, "Successfully removed the product from interest List."),

    // 201 CREATED
    CREATE_INTEREST_SUCCESS(HttpStatus.CREATED, "Successfully added the product to interest List."),


    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
