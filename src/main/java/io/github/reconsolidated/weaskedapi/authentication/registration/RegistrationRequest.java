package io.github.reconsolidated.weaskedapi.authentication.registration;

import lombok.*;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Setter
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
}
