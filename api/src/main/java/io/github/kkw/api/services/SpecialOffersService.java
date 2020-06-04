package io.github.kkw.api.services;

import io.github.kkw.api.db.SpecialOffersRepository;
import io.github.kkw.api.db.dto.SpecialOfferEntity;
import io.github.kkw.api.exceptions.SpecialCodeAlreadyExistsException;
import io.github.kkw.api.exceptions.SpecialOfferCodeNotFound;
import io.github.kkw.api.exceptions.SpecialOffersNotFoundException;
import io.github.kkw.api.model.SpecialOffer;
import io.github.kkw.api.model.SpecialOfferAddRequest;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<SpecialOffer> showSpecialOffers() throws SpecialOffersNotFoundException {
        List<SpecialOfferEntity> specialOfferEntities = specialOffersRepository.getSpecialOffers();
        if(specialOfferEntities.isEmpty()){
            throw new SpecialOffersNotFoundException("No special code exists");
        }
        return specialOfferEntities.stream().map(SpecialOffer::new).collect(Collectors.toList());
    }

    public void deleteSpecialOffer(String code) throws SpecialOfferCodeNotFound {
        if(!specialOffersRepository.ifSpecialOfferAlreadyExists(code)){
            throw new SpecialOfferCodeNotFound("Cannot find special offer of code: " + code);
        }
        specialOffersRepository.deleteSpecialOffer(code);
    }
}
