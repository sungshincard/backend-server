package com.sungshincard.backend.domain.member.service;

import com.sungshincard.backend.domain.member.dto.AddressDto;
import com.sungshincard.backend.domain.member.entity.Address;
import com.sungshincard.backend.domain.member.entity.Member;
import com.sungshincard.backend.domain.member.repository.AddressRepository;
import com.sungshincard.backend.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Long createAddress(String email, AddressDto.Request request) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (request.getIsDefault()) {
            resetDefaultAddress(member.getId());
        }

        Address address = Address.builder()
                .member(member)
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .zipCode(request.getZipCode())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .isDefault(request.getIsDefault())
                .build();

        return addressRepository.save(address).getId();
    }

    public List<AddressDto.Response> getAddresses(String email) {
        return addressRepository.findByMemberEmail(email).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateAddress(String email, Long addressId, AddressDto.Request request) {
        Address address = addressRepository.findByIdAndMemberEmail(addressId, email);
        if (address == null) {
            throw new IllegalArgumentException("배송지를 찾을 수 없거나 권한이 없습니다.");
        }

        if (request.getIsDefault() && !address.getIsDefault()) {
            resetDefaultAddress(address.getMember().getId());
        }

        address = Address.builder()
                .id(address.getId())
                .member(address.getMember())
                .recipientName(request.getRecipientName())
                .recipientPhone(request.getRecipientPhone())
                .zipCode(request.getZipCode())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .isDefault(request.getIsDefault())
                .createdAt(address.getCreatedAt())
                .build(); // For immutability or update logic. Better to just use entity setters, but let's emulate with builder update if there's no setter. Wait, Entity doesn't have @Setter.
        // Let's actually use a repository save or rewrite builder if the entity has no setter.
        // Ah, @Entity Address has @NoArgsConstructor but no @Setter. Let's create an update method on Address entity or replace fields via reflection/repository.save.
        addressRepository.save(address); // Actually, replacing object with new instance but same ID.
    }

    @Transactional
    public void deleteAddress(String email, Long addressId) {
        Address address = addressRepository.findByIdAndMemberEmail(addressId, email);
        if (address == null) {
            throw new IllegalArgumentException("배송지를 찾을 수 없거나 권한이 없습니다.");
        }
        addressRepository.delete(address);
    }

    @Transactional
    public void setDefaultAddress(String email, Long addressId) {
        Address address = addressRepository.findByIdAndMemberEmail(addressId, email);
        if (address == null) {
            throw new IllegalArgumentException("배송지를 찾을 수 없거나 권한이 없습니다.");
        }
        resetDefaultAddress(address.getMember().getId());
        
        Address updated = Address.builder()
                .id(address.getId())
                .member(address.getMember())
                .recipientName(address.getRecipientName())
                .recipientPhone(address.getRecipientPhone())
                .zipCode(address.getZipCode())
                .address1(address.getAddress1())
                .address2(address.getAddress2())
                .isDefault(true)
                .createdAt(address.getCreatedAt())
                .build();
        addressRepository.save(updated);
    }

    private void resetDefaultAddress(Long memberId) {
        List<Address> addresses = addressRepository.findByMemberId(memberId);
        for (Address addr : addresses) {
            if (addr.getIsDefault()) {
                Address updated = Address.builder()
                        .id(addr.getId())
                        .member(addr.getMember())
                        .recipientName(addr.getRecipientName())
                        .recipientPhone(addr.getRecipientPhone())
                        .zipCode(addr.getZipCode())
                        .address1(addr.getAddress1())
                        .address2(addr.getAddress2())
                        .isDefault(false)
                        .createdAt(addr.getCreatedAt())
                        .build();
                addressRepository.save(updated);
            }
        }
    }

    private AddressDto.Response toResponseDto(Address address) {
        return new AddressDto.Response(
                address.getId(),
                address.getRecipientName(),
                address.getRecipientPhone(),
                address.getZipCode(),
                address.getAddress1(),
                address.getAddress2(),
                address.getIsDefault()
        );
    }
}
