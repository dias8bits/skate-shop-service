package com.skateshop.service.user;

import com.skateshop.commons.UserUtils;
import com.skateshop.domain.user.User;
import com.skateshop.exception.CpfAlreadyExistsException;
import com.skateshop.exception.EmailAlreadyExistsException;
import com.skateshop.exception.PhoneAlreadyExistsException;
import com.skateshop.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private UserUtils userUtils;

    @Mock
    private UserRepository userRepository;

    private List<User> userList;

    @BeforeEach
    void init() {
        userList = userUtils.getUserList();
    }

    @Test
    @DisplayName("findAll returns a list with all users when param is null")
    @Order(1)
    void findAll_returnsAllUsers_whenSuccessful() {
        BDDMockito.when(userRepository.findAll()).thenReturn(userList);

        var users = userService.findAll(null);

        Assertions.assertThat(users).isNotNull().hasSameElementsAs(userList);
    }


    @Test
    @DisplayName("findById returns an user if exists")
    @Order(2)
    void findById_returnsAnUsers_whenExists() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var userFound = userService.findUserByIdOrElseThrow(user.getId());

        Assertions.assertThat(userFound).isEqualTo(user);
    }

    @Test
    @DisplayName("findById returns exception if user is not found")
    @Order(3)
    void findById_returnsNotFound_whenUserNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> userService.findUserByIdOrElseThrow(user.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("findByCpf returns an user if exists")
    @Order(4)
    void findByCpf_returnsAnUsers_whenExists() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findUserByCpfIgnoreCase(user.getCpf())).thenReturn(Optional.of(user));

        var userFound = userService.findUserByCpfOrElseThrow(user.getCpf());

        Assertions.assertThat(userFound).isEqualTo(user);
    }

    @Test
    @DisplayName("findByCpf  returns exception if user is not found")
    @Order(5)
    void findByCpf_returnsNotFound_whenUserNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findUserByCpfIgnoreCase(user.getCpf())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> userService.findUserByCpfOrElseThrow(user.getCpf()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save create an user")
    @Order(6)
    void save_createAnUsers_whenSuccessful() {
        var user = userUtils.newUser();

        BDDMockito.when(userRepository.save(user)).thenReturn(user);

        var userSaved = userService.save(user);

        Assertions.assertThat(userSaved).isEqualTo(user).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete delete an user when exists")
    @Order(7)
    void delete_deleteAnUser_whenExists() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        BDDMockito.doNothing().when(userRepository).delete(user);

        Assertions.assertThatNoException().isThrownBy(() -> userService.delete(user));
    }

    @Test
    @DisplayName("delete returns exception if user is not found")
    @Order(8)
    void delete_returnsNotFound_whenUserNotFound() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> userService.delete(user))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updated an user when exists")
    @Order(9)
    void update_updateAnUser_whenExists() {
        var userToUpdate = userList.getFirst().withFirstName("caleb");
        var id = userToUpdate.getId();
        var email = userToUpdate.getEmail();
        var cpf = userToUpdate.getCpf();
        var phone = userToUpdate.getPhone();

        BDDMockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(userRepository.existsByCpfAndIdNot(cpf, id)).thenReturn(false);
        BDDMockito.when(userRepository.existsByEmailAndIdNot(email, id)).thenReturn(false);
        BDDMockito.when(userRepository.existsByPhoneAndIdNot(phone, id)).thenReturn(false);

        Assertions.assertThatNoException().isThrownBy(() -> userService.update(userToUpdate, id));
    }

    @Test
    @DisplayName("update updated selected fields")
    @Order(10)
    void update_updateSelectFields_whenExists() {
        var userToUpdate = userList.getFirst().withFirstName("caleb");
        var id = userToUpdate.getId();
        var email = userToUpdate.getEmail();
        var cpf = userToUpdate.getCpf();
        var phone = userToUpdate.getPhone();

        BDDMockito.when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.when(userRepository.existsByCpfAndIdNot(cpf, id)).thenReturn(false);
        BDDMockito.when(userRepository.existsByEmailAndIdNot(email, id)).thenReturn(false);
        BDDMockito.when(userRepository.existsByPhoneAndIdNot(phone, id)).thenReturn(false);

        Assertions.assertThatNoException().isThrownBy(() -> userService.updateSelectedFields(userToUpdate, id));
    }

    @Test
    @DisplayName("update returns EmailAlreadyExistsException")
    @Order(11)
    void update_updateException_whenEmailAlreadyExists() {
        var userSaved = userList.getLast();
        var userToSave = userUtils.newUser().withEmail(userSaved.getEmail());
        var id = userToSave.getId();
        var email = userToSave.getEmail();

        BDDMockito.when(userRepository.findById(userToSave.getId())).thenReturn(Optional.of(userToSave));
        BDDMockito.when(userRepository.existsByEmailAndIdNot(email, id)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.update(userToSave, id))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update returns CpfAlreadyExistsException")
    @Order(12)
    void update_updateException_whenCpfAlreadyExists() {
        var userSaved = userList.getLast();
        var userToSave = userUtils.newUser().withCpf(userSaved.getCpf());
        var id = userToSave.getId();
        var cpf = userToSave.getCpf();

        BDDMockito.when(userRepository.findById(userToSave.getId())).thenReturn(Optional.of(userToSave));
        BDDMockito.when(userRepository.existsByCpfAndIdNot(cpf, id)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.update(userToSave, id))
                .isInstanceOf(CpfAlreadyExistsException.class);
    }

    @Test
    @DisplayName("update returns PhoneAlreadyExistsException")
    @Order(13)
    void update_updateException_whenPhoneAlreadyExists() {
        var userSaved = userList.getLast();
        var userToSave = userUtils.newUser().withPhone(userSaved.getPhone());
        var id = userToSave.getId();
        var phone = userToSave.getPhone();

        BDDMockito.when(userRepository.findById(userToSave.getId())).thenReturn(Optional.of(userToSave));
        BDDMockito.when(userRepository.existsByPhoneAndIdNot(phone, id)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.update(userToSave, id))
                .isInstanceOf(PhoneAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save returns EmailAlreadyExistsException")
    @Order(14)
    void save_saveException_whenEmailAlreadyExists() {
        var user = userList.getFirst();
        var userToSave = userUtils.newUser().withEmail(user.getEmail());
        var email = userToSave.getEmail();

        BDDMockito.when(userRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.save(userToSave))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save returns CpfAlreadyExistsException")
    @Order(15)
    void save_saveException_whenCpfAlreadyExists() {
        var user = userList.getFirst();
        var userToSave = userUtils.newUser().withCpf(user.getCpf());
        var cpf = userToSave.getCpf();

        BDDMockito.when(userRepository.existsByCpfIgnoreCase(cpf)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.save(userToSave))
                .isInstanceOf(CpfAlreadyExistsException.class);
    }

    @Test
    @DisplayName("save returns PhoneAlreadyExistsException")
    @Order(16)
    void save_saveException_whenPhoneAlreadyExists() {
        var user = userList.getFirst();
        var userToSave = userUtils.newUser().withPhone(user.getPhone());
        var phone = userToSave.getPhone();

        BDDMockito.when(userRepository.existsByPhoneIgnoreCase(phone)).thenReturn(true);

        Assertions.assertThatException()
                .isThrownBy(() -> userService.save(userToSave))
                .isInstanceOf(PhoneAlreadyExistsException.class);
    }

    @Test
    @DisplayName("findAll returns a list with a single user when param is not null")
    @Order(17)
    void findAll_returnsSingleUser_whenIdIsNotNull() {
        var user = userList.getFirst();

        BDDMockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        var users = userService.findAll(user.getId());

        Assertions.assertThat(users).isNotNull().containsExactly(user);
    }
}