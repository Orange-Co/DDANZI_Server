package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    ApiResponse<?> search(@RequestHeader(value = "devicetoken", required = false) String devicetoken,
                          @RequestParam(value = "keyword", required = false) String keyword) {
        if (keyword == null)
            return searchService.searchPage(devicetoken);
        else
            return searchService.searchKeyword(keyword);
    }
}