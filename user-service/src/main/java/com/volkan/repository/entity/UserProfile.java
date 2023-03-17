package com.volkan.repository.entity;

import com.volkan.repository.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@SuperBuilder // Builder, bir sınıftan nesne türetmek için özel oluşturulmuş bir method
@Data // Data,get, set methodlarını tanımlar
@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
@Entity
public class UserProfile extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String adres;
    private String about;
    @Enumerated(EnumType.STRING)
    @Builder.Default()
    private EStatus status = EStatus.PENDING;

}
