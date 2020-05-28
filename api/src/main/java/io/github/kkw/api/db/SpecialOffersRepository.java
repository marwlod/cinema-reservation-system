package io.github.kkw.api.db;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@Repository
public class SpecialOffersRepository {
    private final EntityManager entityManager;

    public SpecialOffersRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Transactional
    public void addSpecialOffer(final String code, final int percentage){
        entityManager
                .createNativeQuery("INSERT INTO special_offers(code, percentage)" +
                "VALUES (?,?)")
                .setParameter(1,code)
                .setParameter(2, percentage)
                .executeUpdate();
    }

    @Transactional
    public boolean ifSpecialOfferAlreadyExists(final String code){
        BigInteger matches = (BigInteger) entityManager
                .createNativeQuery("SELECT IF((SELECT COUNT(*) FROM special_offers WHERE code = ?) > 0, TRUE, FALSE)")
                .setParameter(1, code)
                .getSingleResult();
        return matches.intValue() == 1;
    }
}
