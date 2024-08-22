package co.orange.ddanzi.controller;

import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping
    ApiResponse<?> getMyPage(){
        return myPageService.getMyPage();
    }

    @GetMapping("order")
    ApiResponse<?> getMyOrder(){
        return myPageService.getMyOrder();
    }


    @GetMapping("/interest")
    ApiResponse<?> getInterest(){
        return myPageService.getInterest();
    }
}
