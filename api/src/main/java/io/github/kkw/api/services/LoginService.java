package io.github.kkw.api.services;

import io.github.kkw.api.db.LoginRepository;
import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import io.github.kkw.api.model.ClientId;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(final LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public ClientId registerClient(String email,
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
        return new ClientId(loginRepository.getClientId(email));
    }

    public ClientId loginClient(String email, String password) throws WrongPasswordException {
        if (!loginRepository.emailMatchesPassword(email, password)) {
            throw new WrongPasswordException("Wrong password");
        }
        loginRepository.loginClient(email, password);
        return new ClientId(loginRepository.getClientId(email));
    }

    public void logoutClient(String email) {
        loginRepository.logoutClient(email);
    }

    public void verifyEmailExists(String email) throws ClientNotFoundException {
        if (!loginRepository.clientExists(email)) {
            throw new ClientNotFoundException("Client with this email not found");
        }
    }
}
