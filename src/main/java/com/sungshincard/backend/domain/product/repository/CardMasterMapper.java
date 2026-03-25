package com.sungshincard.backend.domain.product.repository;

import com.sungshincard.backend.domain.product.dto.CardMasterDto;
import com.sungshincard.backend.domain.product.dto.CardMasterSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CardMasterMapper {
    List<CardMasterDto> searchCardMasters(CardMasterSearchDto searchDto);
    long countCardMasters(CardMasterSearchDto searchDto);
}
