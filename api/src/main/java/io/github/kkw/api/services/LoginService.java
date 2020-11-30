package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.ClientExistsException;
import io.github.kkw.api.exceptions.ClientNotFoundException;
import io.github.kkw.api.exceptions.ClientNotLoggedInException;
import io.github.kkw.api.exceptions.NotAdminException;
import io.github.kkw.api.exceptions.WrongPasswordException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.Profile;

import java.util.Optional;

public interface LoginService {
    /**
     *
     * @param email email of client to register
     * @param password password of client to register
     * @param name name of client to register
     * @param surname surname of client to register
     * @return ID of newly registered client
     * @throws ClientExistsException client with this email already exists
     */
    ClientId registerClient(String email,
                            String password,
                            Optional<String> name,
                            Optional<String> surname) throws ClientExistsException;

    /**
     *
     * @param email email of existing client
     * @param password password of existing client
     * @return ID of client that was just logged in
     * @throws WrongPasswordException when password doesn't match the email
     */
    ClientId loginClient(String email, String password) throws WrongPasswordException;

    /**
     *
     * @param clientId ID of client to logout
     */
    void logoutClient(ClientId clientId);

    /**
     *
     * @param email email to verify
     * @throws ClientNotFoundException when client with this email was not registered
     */
    void verifyEmailExists(String email) throws ClientNotFoundException;

    /**
     *
     * @param clientId ID of client to verify if is logged in
     * @throws ClientNotLoggedInException when client with this ID is not logged in
     */
    void verifyClientLoggedIn(ClientId clientId) throws ClientNotLoggedInException;

    /**
     *
     * @param clientId ID of client to verify if is an admin
     * @throws NotAdminException when client with this ID is not an admin
     */
    void verifyAdmin(ClientId clientId) throws NotAdminException;

    /**
     *
     * @param clientId ID of client to show profile for
     * @return profile of client with this ID
     */
    Profile showProfile(ClientId clientId);
}
