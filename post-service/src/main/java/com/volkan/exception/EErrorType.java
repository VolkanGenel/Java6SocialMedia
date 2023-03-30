package com.volkan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;


@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
@Getter
public enum EErrorType {
    INTERNAL_ERROR(5300,"Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4300,"Parametre Hatası", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4310,"Böyle bie kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4311,"Geçersiz Token",HttpStatus.BAD_REQUEST),


    ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
