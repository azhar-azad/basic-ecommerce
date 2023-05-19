package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.auth.AppUserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends PagingAndSortingRepository<AppUserEntity, Long> {

    Optional<AppUserEntity> findByUid(String uid);
    Optional<List<AppUserEntity>> findByRoleId(Long roleId);
    Optional<AppUserEntity> findByEmail(String email);
    Optional<AppUserEntity> findByUsername(String username);
}
