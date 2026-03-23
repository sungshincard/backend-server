package com.sungshincard.backend.domain.product.entity;

import com.sungshincard.backend.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "pokemon")
public class Pokemon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dex_number", nullable = false, unique = true)
    private Integer dexNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String type;

    @Column(length = 100)
    private String region;

    @Column(name = "image_url", length = 500)
    private String imageUrl;
}
