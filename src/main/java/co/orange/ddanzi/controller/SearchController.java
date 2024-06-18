package co.orange.ddanzi.controller;

import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping()
    ApiResponse<?> searchKeyword(@RequestParam("keyword") String keyword) {
        return searchService.searchKeyword(keyword);
    }
}
