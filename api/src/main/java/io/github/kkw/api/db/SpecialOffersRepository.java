package io.github.kkw.api.db;

import io.github.kkw.api.db.dto.SpecialOfferEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;

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

    @Transactional
    public List<SpecialOfferEntity> getSpecialOffers() {
        return (List<SpecialOfferEntity>) entityManager
                .createNativeQuery("SELECT special_offer_id, code, percentage FROM special_offers")
                .getResultList();
    }

}
