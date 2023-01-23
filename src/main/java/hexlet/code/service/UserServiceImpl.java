package hexlet.code.service;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public final User createNewUser(final UserDto userDto) {

        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public final User updateUser(long id, UserDto userDto) {

        final User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    @Override
    public final User getUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    //@Override
    //public List<User> getUsers() {
    //    return userRepository.findAll();
    //}

    @Override
    public final void deleteUser(long id) {
        final User deleteUser = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userRepository.delete(deleteUser);
    }
}
