package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.ProfileEntity;
import io.github.kkw.api.db.dto.SeatEntity;
import io.github.kkw.api.model.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                .createNativeQuery("INSERT INTO client (email, password, name, surname, is_admin, logged_until, register_date) " +
                        "VALUES (?,?,?,?,?,?,?)")
                .setParameter(1, email)
                .setParameter(2, password)
                .setParameter(3, name)
                .setParameter(4, surname)
                .setParameter(5, 0) // not an admin
                .setParameter(6, Instant.now().plus(20, ChronoUnit.MINUTES)) // logged in for 20mins
                .setParameter(7, Instant.now())
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

    @Transactional
    public void logoutClient(int clientId) {
        entityManager
                .createNativeQuery("UPDATE client SET logged_until=? WHERE client_id=?")
                .setParameter(1, Instant.now())
                .setParameter(2, clientId)
                .executeUpdate();
    }

    @Transactional
    public int getClientId(String email) {
        return (int) entityManager
                .createNativeQuery("SELECT client_id FROM client WHERE email=?")
                .setParameter(1, email)
                .getSingleResult();
    }

    @Transactional
    public boolean isClientLoggedIn(int clientId) {
        BigInteger matches = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM client WHERE client_id = ? AND logged_until > ?) > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .setParameter(2, Instant.now())
                .getSingleResult();
        return matches.intValue() == 1;
    }

    @Transactional
    public void extendLogin(int clientId) {
        final Instant oldValidUntil = ((Timestamp) entityManager
                .createNativeQuery("SELECT logged_until FROM client WHERE client_id = ?")
                .setParameter(1, clientId)
                .getSingleResult()).toInstant();
        final Instant nowPlus20Mins = Instant.now().plus(20, ChronoUnit.MINUTES);
        final Instant longerTime = oldValidUntil.compareTo(nowPlus20Mins) > 0 ? oldValidUntil : nowPlus20Mins;
        entityManager
                .createNativeQuery("UPDATE client SET logged_until=? WHERE client_id=?")
                .setParameter(1, longerTime)
                .setParameter(2, clientId)
                .executeUpdate();
    }

    @Transactional
    public boolean isAdmin(int clientId) {
        BigInteger matches = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM client WHERE client_id = ? AND is_admin = 1) > 0, TRUE, FALSE)")
                .setParameter(1, clientId)
                .getSingleResult();
        return matches.intValue() == 1;
    }

    @Transactional
    public ProfileEntity getProfile(int clientId) {
        return (ProfileEntity) entityManager
                .createNativeQuery("SELECT client_id, email, name, surname, is_admin FROM client " +
                                "WHERE client_id = ?",
                        ProfileEntity.class)
                .setParameter(1, clientId)
                .getSingleResult();
    }

    @Transactional
    public int getTotalClientsAtTheMoment(){
        BigInteger totalClients  = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) from client")
                .getSingleResult();
        return totalClients.intValue();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<ProfileEntity> getAllProfiles(){
        return (List<ProfileEntity>) entityManager
                .createNativeQuery("SELECT client_id, email, name, surname, is_admin from client", ProfileEntity.class)
                .getResultList();
    }

    @Transactional
    public int newClientsRegistered(Instant from, Instant to){
        BigInteger newClients = (BigInteger) entityManager
                .createNativeQuery("SELECT COUNT(*) from client WHERE register_date>=? AND register_date<=?")
                .setParameter(1,Timestamp.from(from))
                .setParameter(2,Timestamp.from(to))
                .getSingleResult();
        return newClients.intValue();
    }

}
