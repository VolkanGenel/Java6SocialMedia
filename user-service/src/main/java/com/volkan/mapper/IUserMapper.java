package com.volkan.mapper;

import com.volkan.dto.request.NewCreateUserRequestDto;
import com.volkan.dto.request.UpdateEmailOrUsernameRequestDto;
import com.volkan.dto.request.UpdateUserRequestDto;
import com.volkan.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {
    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);
    UserProfile toUserProfile (final NewCreateUserRequestDto dto);
    UpdateEmailOrUsernameRequestDto toUpdateEmailOrUsernameRequestDto (final UpdateUserRequestDto dto);
}
