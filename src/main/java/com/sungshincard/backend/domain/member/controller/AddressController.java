package com.sungshincard.backend.domain.member.controller;

import com.sungshincard.backend.common.dto.ApiResponse;
import com.sungshincard.backend.domain.member.dto.AddressDto;
import com.sungshincard.backend.domain.member.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/addresses")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ApiResponse<List<AddressDto.Response>> getAddresses(Principal principal) {
        List<AddressDto.Response> addresses = addressService.getAddresses(principal.getName());
        return ApiResponse.success(addresses, "배송지 목록 조회 성공.");
    }

    @PostMapping
    public ApiResponse<Long> createAddress(
            Principal principal,
            @RequestBody @Valid AddressDto.Request request) {
        Long addressId = addressService.createAddress(principal.getName(), request);
        return ApiResponse.success(addressId, "배송지가 추가되었습니다.");
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> updateAddress(
            Principal principal,
            @PathVariable Long id,
            @RequestBody @Valid AddressDto.Request request) {
        addressService.updateAddress(principal.getName(), id, request);
        return ApiResponse.success(null, "배송지가 수정되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAddress(
            Principal principal,
            @PathVariable Long id) {
        addressService.deleteAddress(principal.getName(), id);
        return ApiResponse.success(null, "배송지가 삭제되었습니다.");
    }

    @PatchMapping("/{id}/default")
    public ApiResponse<Void> setDefaultAddress(
            Principal principal,
            @PathVariable Long id) {
        addressService.setDefaultAddress(principal.getName(), id);
        return ApiResponse.success(null, "기본 배송지로 설정되었습니다.");
    }
}
