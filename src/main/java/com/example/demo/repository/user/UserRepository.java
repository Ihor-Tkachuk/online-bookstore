package com.example.demo.repository.user;

import com.example.demo.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends
        JpaRepository<User, Long>,
        JpaSpecificationExecutor<User>,
        PagingAndSortingRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
