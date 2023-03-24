package com.volkan.repository;

import com.volkan.repository.entity.Auth;
import com.volkan.repository.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    @Query(value = "select COUNT(a)>0 from Auth a where a.username=?1")
    boolean isUsername(String username);

    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);
    Boolean existsByUsernameAndPassword(String username, String password);
    List<Auth> findAllByRole(ERole role);
}
