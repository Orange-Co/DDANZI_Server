package co.orange.ddanzi.controller;

import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.global.common.response.ApiResponse;
import co.orange.ddanzi.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
@RestController
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/confirm")
    ApiResponse<?> confirmProduct(@RequestBody ConfirmProductRequestDto requestDto){
        return itemService.confirmProduct(requestDto);
    }

    @PostMapping
    ApiResponse<?> saveItem(@RequestBody SaveItemRequestDto requestDto){
        return itemService.saveItem(requestDto);
    }

}
