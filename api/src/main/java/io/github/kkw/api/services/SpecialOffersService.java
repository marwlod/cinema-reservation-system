package io.github.kkw.api.services;

import io.github.kkw.api.db.SpecialOffersRepository;
import io.github.kkw.api.exceptions.SpecialCodeAlreadyExistsException;
import io.github.kkw.api.model.SpecialOfferAddRequest;
import org.springframework.stereotype.Service;

@Service
public class SpecialOffersService {

    private final SpecialOffersRepository specialOffersRepository;

    public SpecialOffersService(SpecialOffersRepository specialOffersRepository){
        this.specialOffersRepository=specialOffersRepository;
    }

    public void addSpecialOffer(SpecialOfferAddRequest specialOffer) throws SpecialCodeAlreadyExistsException {
        if(specialOffersRepository.ifSpecialOfferAlreadyExists(specialOffer.getCode())){
            throw new SpecialCodeAlreadyExistsException("Special offer code already exists");
        }
        specialOffersRepository.addSpecialOffer(specialOffer.getCode(),specialOffer.getPercentage());
    }
}
