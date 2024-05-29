package com.mmdevelopement.crm.domain.user.repository;

import com.mmdevelopement.crm.domain.user.entity.UserProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Integer> {
    UserProfileEntity findByUserId(Integer userId);
}
