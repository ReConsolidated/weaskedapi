package io.github.reconsolidated.weaskedapi.authentication.appUser;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, code = HttpStatus.CONFLICT, reason="User already exists")
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException() {
        super();
    }
}
