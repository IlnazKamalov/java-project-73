package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfigForTest;
import hexlet.code.dto.LoginDto;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfigForTest.TEST_PROFILE;
import static hexlet.code.config.security.WebSecurityConfig.LOGIN;
import static hexlet.code.controller.UserController.ID;
import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static hexlet.code.utils.TestUtils.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hexlet.code.utils.TestUtils.fromJson;
import static hexlet.code.utils.TestUtils.asJson;
import static hexlet.code.utils.TestUtils.TEST_USERNAME_2;
import static hexlet.code.utils.TestUtils.TEST_USERNAME;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForTest.class)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestUtils testUtils;

    @BeforeEach
    public final void clear() {

        testUtils.tearDown();
    }

    @Test
    public void registration() throws Exception {

        assertEquals(0, userRepository.count());

        testUtils.regDefaultUser().andExpect(status().isCreated());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void getUserById() throws Exception {

        testUtils.regDefaultUser();

        final User expectedUser = userRepository.findAll().get(0);

        final var response = testUtils.perform(
                        get(BASE_URL + USER_CONTROLLER_PATH + ID, expectedUser.getId()),
                        expectedUser.getEmail())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User user = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getFirstName(), user.getFirstName());
        assertEquals(expectedUser.getLastName(), user.getLastName());
    }

    @Test
    public void getUserByIdFails() throws Exception {

        testUtils.regDefaultUser();

        final User expectedUser = userRepository.findAll().get(0);

        Exception exception = assertThrows(
                Exception.class, () -> testUtils.perform(
                        get(BASE_URL + USER_CONTROLLER_PATH + ID,
                        expectedUser.getId()))
        );

        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));
    }

    @Test
    public void getAllUsers() throws Exception {

        testUtils.regDefaultUser();

        final var response = testUtils.perform(get(BASE_URL + USER_CONTROLLER_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<User> users = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(users).hasSize(1);
    }

    @Test
    public void twiceRegTheSameUserFail() throws Exception {

        testUtils.regDefaultUser().andExpect(status().isCreated());
        testUtils.regDefaultUser().andExpect(status().isUnprocessableEntity());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void login() throws Exception {

        testUtils.regDefaultUser();

        final LoginDto loginDto = new LoginDto(
                testUtils.getTestRegistrationDto().getFirstName(),
                testUtils.getTestRegistrationDto().getLastName(),
                testUtils.getTestRegistrationDto().getEmail(),
                testUtils.getTestRegistrationDto().getPassword()
        );

        final var loginRequest = post(BASE_URL + LOGIN)
                .content(asJson(loginDto))
                .contentType(APPLICATION_JSON);

        testUtils.perform(loginRequest).andExpect(status().isOk());
    }

    @Test
    public void loginFail() throws Exception {

        final LoginDto loginDto = new LoginDto(
                testUtils.getTestRegistrationDto().getFirstName(),
                testUtils.getTestRegistrationDto().getLastName(),
                testUtils.getTestRegistrationDto().getEmail(),
                testUtils.getTestRegistrationDto().getPassword()
        );

        final var loginRequest =
                post(BASE_URL + LOGIN)
                        .content(asJson(loginDto))
                        .contentType(APPLICATION_JSON);

        testUtils.perform(loginRequest).andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUser() throws Exception {

        testUtils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TEST_USERNAME).isPresent()
                ? userRepository.findByEmail(TEST_USERNAME).get().getId() : null;

        final var userDto = new UserDto(
                TEST_USERNAME_2, "newname", "newlastname", "newpwd");

        final var updateRequest =
                put(BASE_URL + USER_CONTROLLER_PATH + ID, userId)
                        .content(asJson(userDto))
                        .contentType(APPLICATION_JSON);

        testUtils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());

        assert userId != null;
        assertTrue(userRepository.existsById(userId));
        assertNull(userRepository.findByEmail(TEST_USERNAME).orElse(null));
        assertNotNull(userRepository.findByEmail(TEST_USERNAME_2).orElse(null));
    }

    @Test
    public void deleteUser() throws Exception {

        testUtils.regDefaultUser();

        final Long userId = userRepository.findByEmail(TEST_USERNAME).isPresent()
                ? userRepository.findByEmail(TEST_USERNAME).get().getId() : null;

        testUtils.perform(
                delete(BASE_URL + USER_CONTROLLER_PATH + ID, userId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }
}
