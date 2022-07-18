package com.csv.repository;

import com.csv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndDeleteStatus(String email, boolean deleteStatus);

    @Modifying
    @Transactional
    @Query(value = "update user set delete_status=false where email=:email", nativeQuery = true)
    void updateIfUserExist(String email);
}
