package com.volkan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


@NoArgsConstructor // Parametresiz constructor tanımlar
@AllArgsConstructor // 1....n kadar olan tüm parametreli constructorları tanımlar
@Getter
public enum EErrorType {
    INTERNAL_ERROR(5100,"Sunucu Hatası", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre Hatası", HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4110,"Kullanıcı adı veya şifre hatalı!!!", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4111,"Böyle bir kullanıcı adı mevcut",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112,"Böyle bie kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    ACTIVATE_CODE_ERROR(4113,"Aktivasyon kod hatası",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4114,"Geçersiz token",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4116,"Token oluşturulamadı",HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_ACTIVE(4115,"Aktive Edilmemiş Hesap!!!",HttpStatus.FORBIDDEN),
                ;

    private int code;
    private String message;
    private HttpStatus httpStatus;
}
