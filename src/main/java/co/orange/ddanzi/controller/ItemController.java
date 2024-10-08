package co.orange.ddanzi.controller;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.common.error.Error;
import co.orange.ddanzi.common.response.ApiResponse;
import co.orange.ddanzi.dto.product.ProductRequestDto;
import co.orange.ddanzi.repository.AddressRepository;
import co.orange.ddanzi.repository.UserRepository;
import co.orange.ddanzi.service.ItemService;
import co.orange.ddanzi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
@RestController
public class ItemController {
    private final ProductService productService;
    private final ItemService itemService;

    @GetMapping("/signed-url")
    ApiResponse<?> createSignedUrl(@RequestParam String fileName) {
        return itemService.createSignedUrl(fileName);
    }

    @PostMapping("/check")
    ApiResponse<?> checkProduct(@RequestBody ProductRequestDto requestDto) {
        return productService.getMostSimilarProduct(requestDto);
    }

    @GetMapping("/product/{id}")
    ApiResponse<?> getProduct(@PathVariable("id") String id) {
        return productService.getProductForItem(id);
    }


    @PostMapping
    ApiResponse<?> saveItem(@RequestBody SaveItemRequestDto requestDto){
        return itemService.saveItem( requestDto);
    }

    @GetMapping("/{id}")
    ApiResponse<?> getItem(@PathVariable("id") String id) {
        return itemService.getItem(id);
    }

    @DeleteMapping("/{id}")
    ApiResponse<?> deleteItem(@PathVariable("id") String id) {
        return itemService.deleteItem(id);
    }

    @GetMapping("/order/{id}")
    ApiResponse<?> getAddressAndOption(@PathVariable("id") String id) {
        return itemService.getAddressAndOption(id);
    }

}
