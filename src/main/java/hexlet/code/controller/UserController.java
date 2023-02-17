package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.UserService;

import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {

    public static final String USER_CONTROLLER_PATH = "/users";

    public static final String ID = "/{id}";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;

    @PostMapping
    @ResponseStatus(CREATED)
    public final User registerNewUser(@RequestBody @Valid final UserDto userDto) {

        return userService.createNewUser(userDto);
    }

    @GetMapping
    public final List<User> getAll() {

        return userRepository.findAll()
                .stream()
                .toList();
    }

    @GetMapping(ID)
    public final User getUserById(@PathVariable final long id) {

        return userRepository.findById(id).isPresent()
                ? userRepository.findById(id).get() : null;
    }

    @PutMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public final User update(@PathVariable final long id,
                       @RequestBody @Valid final UserDto userDto) {

        return userService.updateUser(id, userDto);
    }

    @DeleteMapping(ID)
    @PreAuthorize(ONLY_OWNER_BY_ID)
    public final void delete(@PathVariable final long id) {

        userRepository.deleteById(id);
    }
}
