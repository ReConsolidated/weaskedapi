package io.github.reconsolidated.weaskedapi.authentication.registration;

import lombok.*;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Setter
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;

}
