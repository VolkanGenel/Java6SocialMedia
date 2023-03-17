package com.volkan.repository.entity;

import com.volkan.repository.enums.ERole;
import com.volkan.repository.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * 1- Repository
 * 2- Service
 * 3- Controller katmanları oluşturulacak
 * -register methodu yazılacak ve buna bir endpoint yazılacak
 * bu işlemler request dto ile yapılacak dönüş tipi yine bir response dto olsun
 * @Post mapping ile olsun
 *
 */
@SuperBuilder // Builder, bir sınıftan nesne türetmek için özel oluşturulmuş bir method
@Data // Data,get, set methodlarını tanımlar
@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
@ToString // sınıf için toString methodunu tanımlar
@Entity
public class Auth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String activationCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERole role = ERole.USER;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status=EStatus.PENDING;
}
