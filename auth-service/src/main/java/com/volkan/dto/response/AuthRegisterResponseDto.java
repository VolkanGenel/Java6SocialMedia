package com.volkan.dto.response;

import lombok.*;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder // Builder, bir sınıftan nesne türetmek için özel oluşturulmuş bir method
@Data // Data,get, set methodlarını tanımlar
@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
public class AuthRegisterResponseDto {
    private Long id;
    @NotBlank(message = "Kullanıcı adı boş geçilemez")
    @Size(min=3, max=32)
    String username;
//    @Email(message = "Lütfen geçerli bir email adresi giriniz")
//   String email;
    private String activationCode;
}
