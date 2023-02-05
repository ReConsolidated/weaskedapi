package io.github.reconsolidated.weaskedapi;

import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUserRole;
import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUserService;
import io.github.reconsolidated.weaskedapi.friends.FriendsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FriendsServiceTest {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private FriendsService friendsService;

    @Test
    public void testGetFriends_happy_path() {
        String email1 = "email1@email.com";
        appUserService.signUpUser(new AppUser("user1", "", email1, "", AppUserRole.USER));
        appUserService.enableAppUser(email1);
        Long friend1 = appUserService.getByEmail(email1).orElseThrow().getId();

        String email2 = "email2@email.com";
        appUserService.signUpUser(new AppUser("user2", "", email2, "", AppUserRole.USER));
        appUserService.enableAppUser(email2);
        Long friend2 = appUserService.getByEmail(email2).orElseThrow().getId();

        friendsService.addFriend(friend1, friend2);
        assertThat(friendsService.getFriends(friend1).size()).isEqualTo(1);
        assertThat(friendsService.getFriends(friend1).get(0).getId()).isEqualTo(friend2);
    }

    @Test
    public void testGetFriends_not_a_friend() {
        String email1 = "email111@email.com";
        appUserService.signUpUser(new AppUser("user1", "", email1, "", AppUserRole.USER));
        appUserService.enableAppUser(email1);
        Long friend1 = appUserService.getByEmail(email1).orElseThrow().getId();

        String email2 = "email222@email.com";
        appUserService.signUpUser(new AppUser("user2", "", email2, "", AppUserRole.USER));
        appUserService.enableAppUser(email2);
        Long friend2 = appUserService.getByEmail(email2).orElseThrow().getId();

        assertThat(friendsService.getFriends(friend1).size()).isEqualTo(0);
    }

    @Test
    public void testAddFriend_happy_path() {
        String email1 = "email3@email.com";
        appUserService.signUpUser(new AppUser("user3", "", email1, "", AppUserRole.USER));
        appUserService.enableAppUser(email1);
        Long friend1 = appUserService.getByEmail(email1).orElseThrow().getId();

        String email2 = "email4@email.com";
        appUserService.signUpUser(new AppUser("user4", "", email2, "", AppUserRole.USER));
        appUserService.enableAppUser(email2);
        Long friend2 = appUserService.getByEmail(email2).orElseThrow().getId();

        friendsService.addFriend(friend1, friend2);
        assertThat(friendsService.areFriends(friend1, friend2)).isTrue();
        assertThat(friendsService.areFriends(friend2, friend1)).isTrue();
    }

    @Test
    public void testAddFriend_cant_add_yourself() {
        String email1 = "email5@email.com";
        appUserService.signUpUser(new AppUser("user5", "", email1, "", AppUserRole.USER));
        appUserService.enableAppUser(email1);
        Long friend1 = appUserService.getByEmail(email1).orElseThrow().getId();
        assertThrows(IllegalStateException.class, () -> friendsService.addFriend(friend1, friend1));
    }
}
