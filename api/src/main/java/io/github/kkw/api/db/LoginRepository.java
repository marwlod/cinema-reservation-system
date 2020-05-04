package io.github.kkw.api.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Repository
public class LoginRepository {
    private final EntityManager entityManager;

    public LoginRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void registerClient(final String email,
                               final String password,
                               final String name,
                               final String surname) {
        entityManager
                .createNativeQuery("INSERT INTO client (email, password, name, surname, is_admin, logged_until) " +
                        "VALUES (?,?,?,?,?,?)")
                .setParameter(1, email)
                .setParameter(2, password)
                .setParameter(3, name)
                .setParameter(4, surname)
                .setParameter(5, 0) // not an admin
                .setParameter(6, Instant.now()) // logged until now => not logged in :)
                .executeUpdate();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public boolean clientExists(final String email) {
        List<Object[]> existingIds = entityManager
                .createNativeQuery("SELECT client_id FROM client WHERE email=?")
                .setParameter(1, email)
                .getResultList();
        return !existingIds.isEmpty();
    }

    @Transactional
    public boolean emailMatchesPassword(String email, String password) {
        BigInteger matches = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM client WHERE email = ? AND password = ?) > 0, TRUE, FALSE)")
                .setParameter(1, email)
                .setParameter(2, password)
                .getSingleResult();
        return matches.intValue() == 1;
    }

    @Transactional
    public void loginClient(String email, String password) {
        entityManager
                .createNativeQuery("UPDATE client SET logged_until=? WHERE email=? AND password=?")
                .setParameter(1, Instant.now().plus(20, ChronoUnit.MINUTES))
                .setParameter(2, email)
                .setParameter(3, password)
                .executeUpdate();
    }
}
