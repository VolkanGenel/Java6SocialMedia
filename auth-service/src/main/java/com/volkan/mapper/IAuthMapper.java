package com.volkan.mapper;

import com.volkan.dto.request.AuthRegisterRequestDto;
import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.response.AuthRegisterResponseDto;
import com.volkan.repository.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface IAuthMapper {
    IAuthMapper INSTANCE = Mappers.getMapper(IAuthMapper.class);
    Auth toAuth(final AuthRegisterRequestDto dto);
    AuthRegisterResponseDto toAuthResponseDto (final Auth auth);
    @Mapping(source = "id",target = "authId")
    NewCreateUserRequestDto toNewCreateUserRequestDto(final Auth auth);

}
