package io.github.kkw.api.services;

import io.github.kkw.api.db.LoginRepository;
import io.github.kkw.api.exceptions.ClientExistsException;
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
}
