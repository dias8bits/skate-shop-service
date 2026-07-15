package com.skateshop.service.userService;

import com.skateshop.domain.user.User;
import com.skateshop.exception.CpfAlreadyExistsException;
import com.skateshop.exception.EmailAlreadyExistsException;
import com.skateshop.exception.NotFoundException;
import com.skateshop.exception.PhoneAlreadyExistsException;
import com.skateshop.repository.userRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    private User findUserByIdOrElseThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("user with id %s not found", id)
                ));
    }

    @Transactional
    public User save(User userToSave) {
        validateUniqueFields(userToSave);
        return userRepository.save(userToSave);
    }

    @Transactional
    public User update(User user) {
        findUserByIdOrElseThrow(user.getId());

        validateUniqueFieldsForUpdate(user);

        return userRepository.save(user);
    }

    @Transactional
    public User updateSelectedFields(User user) {
        var userToUpdate = findUserByIdOrElseThrow(user.getId());

        if (user.getFirstName() != null) {
            userToUpdate.setFirstName(user.getFirstName());
        }

        if (user.getLastName() != null) {
            userToUpdate.setLastName(user.getLastName());
        }

        if (user.getPhone() != null) {
            assertThatPhoneDoesNotExistOrElseThrow(user.getPhone(), user.getId());
            userToUpdate.setPhone(user.getPhone());
        }

        if (user.getEmail() != null) {
            assertThatEmailDoesNotExistOrElseThrow(user.getEmail(), user.getId());
            userToUpdate.setEmail(user.getEmail());
        }

        if (user.getCpf() != null) {
            assertThatCpfDoesNotExistOrElseThrow(user.getCpf(), user.getId());
            userToUpdate.setCpf(user.getCpf());
        }

        return userRepository.save(userToUpdate);
    }

    @Transactional
    public void delete(UUID id) {
        var userToDelete = findUserByIdOrElseThrow(id);
        userRepository.delete(userToDelete);
    }

    private void validateUniqueFields (User user) {
        assertThatEmailDoesNotExistOrElseThrow(user.getEmail());
        assertThatCpfDoesNotExistOrElseThrow(user.getCpf());
        assertThatPhoneDoesNotExistOrElseThrow(user.getPhone());
    }


    private void validateUniqueFieldsForUpdate (User user) {
        assertThatEmailDoesNotExistOrElseThrow(user.getEmail(), user.getId());
        assertThatCpfDoesNotExistOrElseThrow(user.getCpf(), user.getId());
        assertThatPhoneDoesNotExistOrElseThrow(user.getPhone(), user.getId());
    }

    private void assertThatEmailDoesNotExistOrElseThrow(String email) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException(
                    String.format("user with email '%s' already exists", email)
            );
        }
    }

    private void assertThatEmailDoesNotExistOrElseThrow(String email, UUID id) {
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException(
                    String.format("user with email '%s' already exists", email)
            );
        }
    }

    private void assertThatCpfDoesNotExistOrElseThrow(String cpf) {
        if (userRepository.existsByCpfIgnoreCase(cpf)) {
            throw new CpfAlreadyExistsException(
                    String.format("user with cpf '%s' already exists", cpf)
            );
        }
    }

    private void assertThatCpfDoesNotExistOrElseThrow(String cpf, UUID id) {
        if (userRepository.existsByCpfAndIdNot(cpf, id)) {
            throw new CpfAlreadyExistsException(
                    String.format("user with cpf '%s' already exists", cpf)
            );
        }
    }

    private void assertThatPhoneDoesNotExistOrElseThrow(String phone) {
        if (userRepository.existsByPhoneIgnoreCase(phone)) {
            throw new PhoneAlreadyExistsException(
                    String.format("user with phone '%s' already exists", phone)
            );
        }
    }

    private void assertThatPhoneDoesNotExistOrElseThrow(String phone, UUID id) {
        if (userRepository.existsByPhoneAndIdNot(phone, id)) {
            throw new PhoneAlreadyExistsException(
                    String.format("user with phone '%s' already exists", phone)
            );
        }
    }

}
