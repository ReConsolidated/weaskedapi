package io.github.reconsolidated.weaskedapi.authentication.Email;

public interface EmailSender {
    void send(String to, String email);
}
