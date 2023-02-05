package io.github.reconsolidated.weaskedapi;

import io.github.reconsolidated.weaskedapi.Events.AddEventDto;
import io.github.reconsolidated.weaskedapi.Events.EventsService;
import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUser;
import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUserRole;
import io.github.reconsolidated.weaskedapi.authentication.AppUser.AppUserService;
import io.github.reconsolidated.weaskedapi.friends.FriendsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddingFriendsToEventIT {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private EventsService eventsService;
    @Autowired
    private FriendsService friendsService;

    @Test
    public void createEventAndInviteAllFriends() {
        String email1 = "email11@email.com";
        appUserService.signUpUser(new AppUser("user1", "", email1, "", AppUserRole.USER));
        appUserService.enableAppUser(email1);
        AppUser eventCreatorUser = appUserService.getByEmail(email1).orElseThrow();

        String email2 = "email12@email.com";
        appUserService.signUpUser(new AppUser("user2", "", email2, "", AppUserRole.USER));
        appUserService.enableAppUser(email2);
        AppUser friend1 = appUserService.getByEmail(email2).orElseThrow();

        String email3 = "email13@email.com";
        appUserService.signUpUser(new AppUser("user3", "", email3, "", AppUserRole.USER));
        appUserService.enableAppUser(email3);
        AppUser friend2 = appUserService.getByEmail(email3).orElseThrow();

        friendsService.addFriend(eventCreatorUser.getId(), friend1.getId());
        friendsService.addFriend(eventCreatorUser.getId(), friend2.getId());

        List<Long> friends = friendsService.getFriends(eventCreatorUser.getId()).stream().map(AppUser::getId).collect(Collectors.toList());

        double latitude = 11;
        double longitude = 12;
        AddEventDto addEventDto = new AddEventDto();
        addEventDto.setColor("#FFFFFF");
        addEventDto.setDate(LocalDateTime.now());
        addEventDto.setDescription("Description");
        addEventDto.setLatitude(latitude);
        addEventDto.setLongitude(longitude);
        addEventDto.setDurationInSeconds(3600);
        addEventDto.setName("Name");
        long eventId = eventsService.createEvent(eventCreatorUser, addEventDto);
        eventsService.addParticipants(eventId, friends);

        assertThat(eventsService.getNearbyEvents(eventCreatorUser, latitude, longitude).size()).isEqualTo(1);
        assertThat(eventsService.getNearbyEvents(friend1, latitude, longitude).size()).isEqualTo(1);
        assertThat(eventsService.getNearbyEvents(friend2, latitude, longitude).size()).isEqualTo(1);

        assertThat(eventsService.getNearbyEvents(eventCreatorUser, latitude, longitude).get(0).getParticipants().contains(friend1)).isTrue();
        assertThat(eventsService.getNearbyEvents(eventCreatorUser, latitude, longitude).get(0).getParticipants().contains(friend2)).isTrue();
        assertThat(eventsService.getNearbyEvents(eventCreatorUser, latitude, longitude).get(0).getOwnerId().equals(eventCreatorUser.getId())).isTrue();
    }
}
