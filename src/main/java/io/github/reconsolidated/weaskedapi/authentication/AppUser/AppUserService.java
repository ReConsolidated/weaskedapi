package io.github.reconsolidated.weaskedapi.authentication.AppUser;

import io.github.reconsolidated.weaskedapi.authentication.Registration.token.ConfirmationToken;
import io.github.reconsolidated.weaskedapi.authentication.Registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE.formatted(username)));
    }

    public Optional<AppUser> getById(long id) {
        return appUserRepository.findById(id);
    }

    public String signUpUser(AppUser appUser) {
        boolean userExists = appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent();
        appUser.setEnabled(true);
        if (userExists) {
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);


        return token;
    }

    public void enableAppUser(String email) {
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(() -> new IllegalStateException("AppUser with such email not found"));
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }

    public Optional<AppUser> getByEmail(String friendEmail) {
        return appUserRepository.findByEmail(friendEmail);
    }
}
