package com.lamnd.zerotohero.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    private String name;

    private String description;
}
