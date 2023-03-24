package com.volkan.mapper;

import com.volkan.rabbitmq.model.RegisterElasticModel;
import com.volkan.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IElasticMapper {
    IElasticMapper INSTANCE = Mappers.getMapper(IElasticMapper.class);
    UserProfile toUserProfile(final RegisterElasticModel model);
}
