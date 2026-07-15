package com.skateshop.repository.userRepository;

import com.skateshop.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailAndIdNot(String email, UUID id);

    boolean existsByCpfIgnoreCase(String cpf);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByPhoneIgnoreCase(String phone);

    boolean existsByPhoneAndIdNot(String phone, UUID id);

}
