package com.volkan.repository;

import com.volkan.repository.entity.UserProfile;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserProfileRepository extends ElasticsearchRepository<UserProfile,String> {

    Optional<UserProfile> findOptionalByUsernameIgnoreCase (String username);

    Optional<UserProfile> findOptionalByAuthId(Long authId);
}
