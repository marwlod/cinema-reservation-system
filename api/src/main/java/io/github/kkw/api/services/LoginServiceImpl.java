package io.github.kkw.api.services;

import io.github.kkw.api.db.LoginRepository;
import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.NotAdminException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private final LoginRepository loginRepository;

    public LoginServiceImpl(final LoginRepository loginRepository) {
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

    public void logoutClient(ClientId clientId) {
        loginRepository.logoutClient(clientId.getClientId());
    }

    public void verifyEmailExists(String email) throws ClientNotFoundException {
        if (!loginRepository.clientExists(email)) {
            throw new ClientNotFoundException("Client with this email not found");
        }
    }

    // TODO add Spring Security for token verification
    public void verifyClientLoggedIn(ClientId clientId) throws ClientNotLoggedInException {
        if (!loginRepository.isClientLoggedIn(clientId.getClientId())) {
            throw new ClientNotLoggedInException("Client doesn't exist or not logged in");
        }
        loginRepository.extendLogin(clientId.getClientId());
    }

    public void verifyAdmin(ClientId clientId) throws NotAdminException {
        if (!loginRepository.isAdmin(clientId.getClientId())) {
            throw new NotAdminException("Client is not an admin");
        }
    }

    public Profile showProfile(ClientId clientId) {
        return new Profile(loginRepository.getProfile(clientId.getClientId()));
    }
}
