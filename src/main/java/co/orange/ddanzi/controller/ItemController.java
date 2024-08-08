package co.orange.ddanzi.controller;

import co.orange.ddanzi.domain.user.User;
import co.orange.ddanzi.dto.item.ConfirmProductRequestDto;
import co.orange.ddanzi.dto.item.SaveItemRequestDto;
import co.orange.ddanzi.global.common.error.Error;
import co.orange.ddanzi.global.common.response.ApiResponse;
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

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @PostMapping("/confirm")
    ApiResponse<?> confirmProduct(@RequestBody ConfirmProductRequestDto requestDto){
        return productService.confirmProduct(requestDto);
    }

    @PostMapping
    ApiResponse<?> saveItem(@RequestBody SaveItemRequestDto requestDto){
        User user = userRepository.findById(1L).orElse(null);
        if(user.getAuthentication() == null)
            return ApiResponse.onFailure(Error.AUTHENTICATION_INFO_NOT_FOUND, null);
        if(addressRepository.findByUser(user) == null)
            return ApiResponse.onFailure(Error.ADDRESS_NOT_FOUND, null);
        if(requestDto.getDueDate().isBefore(LocalDate.now()))
            return ApiResponse.onFailure(Error.DUE_DATE_IS_INCORRECT, null);
        return itemService.saveItem(user, requestDto);
    }

}
