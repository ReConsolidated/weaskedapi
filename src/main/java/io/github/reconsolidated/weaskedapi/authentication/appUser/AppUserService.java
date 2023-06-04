package io.github.reconsolidated.weaskedapi.authentication.appUser;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;


@Service
@Validated
public class AppUserService {

    private final static String USER_NOT_FOUND_MESSAGE =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final String defaultImageUrl = "";

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public Optional<AppUser> findUserById(Long appUserId) {
        return appUserRepository.findById(appUserId);
    }

    public Optional<AppUser> findUserByEmail(String inviteeEmail) {
        return appUserRepository.findByEmail(inviteeEmail);
    }

    public AppUser getOrCreateUser(String keycloakId, String email, String userName, String firstName, String lastName) {
        return appUserRepository.findByKeycloakId(keycloakId).orElseGet(() -> {
            AppUser appUser = new AppUser();
            appUser.setKeycloakId(keycloakId);
            appUser.setEmail(email);
            appUser.setFirstName(firstName);
            appUser.setLastName(lastName);
            appUser.setUserName(userName);
            appUser.setImageUrl(defaultImageUrl);

            return appUserRepository.save(appUser);
        });
    }

    public void setFirstName(AppUser user, String name) {
        user.setFirstName(name);
        appUserRepository.save(user);
    }

    public void setLastName(AppUser user, String name) {
        user.setLastName(name);
        appUserRepository.save(user);
    }

    public AppUser getUser(Long appUserId) {
        return appUserRepository.findById(appUserId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void setImageUrl(AppUser user, String imageUrl) {
        user.setImageUrl(imageUrl);
        appUserRepository.save(user);
    }

    public void deleteUser(AppUser user) {
        appUserRepository.delete(user);
    }
}
