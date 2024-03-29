package hexlet.code.service;

import hexlet.code.config.security.WebSecurityConfig;
import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createNewUser(final UserDto userDto) {

        final User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(long id, UserDto userDto) {

        final User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setFirstName(userDto.getFirstName());
        userToUpdate.setLastName(userDto.getLastName());
        userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepository.save(userToUpdate);
    }

    @Override
    public String getCurrentUserName() {

        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public User getCurrentUser() {

        return userRepository.findByEmail(getCurrentUserName()).get();
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .map(this::buildSpringUser)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'username': " + username));
    }

    private UserDetails buildSpringUser(final User user) {

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                WebSecurityConfig.DEFAULT_AUTHORITIES
        );
    }
}
