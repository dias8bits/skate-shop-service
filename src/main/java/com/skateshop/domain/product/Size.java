package com.skateshop.domain.product;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sizes")
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "size", nullable = false)
    private String size;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, unique = true)
    private Category category;

}
