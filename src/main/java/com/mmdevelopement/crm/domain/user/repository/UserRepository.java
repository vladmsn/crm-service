package com.mmdevelopement.crm.domain.user.repository;

import com.mmdevelopement.crm.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUserGuid(String userGuid);
}
