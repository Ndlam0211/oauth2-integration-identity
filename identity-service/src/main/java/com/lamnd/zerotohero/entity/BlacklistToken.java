package com.lamnd.zerotohero.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistToken {
    @Id
    private String id;

    private Date expiryTime;
}
