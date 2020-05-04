package io.github.kkw.api.services;

import io.github.kkw.api.db.LoginRepository;
import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(final LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void registerClient(String email,
                               String password,
                               Optional<String> name,
                               Optional<String> surname) throws ClientExistsException {
        if (loginRepository.clientExists(email)) {
            throw new ClientExistsException("Client with this email already exists");
        }
        loginRepository.registerClient(
                email,
                password,
                name.isEmpty() ? null : name.get(),
                surname.isEmpty() ? null : surname.get());
    }

    public void loginClient(String email, String password) throws ClientNotFoundException, WrongPasswordException {
        if (!loginRepository.clientExists(email)) {
            throw new ClientNotFoundException("Client with this email not found");
        }
        if (!loginRepository.emailMatchesPassword(email, password)) {
            throw new WrongPasswordException("Wrong password");
        }
        loginRepository.loginClient(email, password);
    }
}
