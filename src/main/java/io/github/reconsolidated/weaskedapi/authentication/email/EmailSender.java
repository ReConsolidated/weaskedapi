package io.github.reconsolidated.weaskedapi.authentication.email;

public interface EmailSender {
    void send(String to, String email);
}
