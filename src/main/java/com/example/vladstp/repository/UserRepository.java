package com.example.vladstp.repository;

import com.example.vladstp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);//метод который возвращает пользователя (см. документацию)
}
