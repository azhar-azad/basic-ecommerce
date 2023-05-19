package com.azad.basicecommerce.repository;

import com.azad.basicecommerce.model.auth.RoleEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);
}
