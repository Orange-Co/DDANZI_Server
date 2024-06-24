package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
@RestController
public class ItemController {
    private final ItemService itemService;
    @GetMapping("/confirm")
    ApiResponse<?> confirmProduct(@RequestBody ConfirmProductRequestDto requestDto){
        return itemService.confirmProduct(requestDto);
    }
}
