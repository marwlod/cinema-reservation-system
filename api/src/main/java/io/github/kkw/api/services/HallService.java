package io.github.kkw.api.services;

import io.github.kkw.api.db.HallRepository;
import io.github.kkw.api.db.dto.HallEntity;
import io.github.kkw.api.exceptions.AvailableCinemaHallsNotFoundException;
import io.github.kkw.api.exceptions.CinemaHallNotFoundException;
import io.github.kkw.api.model.Hall;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    public List<Hall> showCinemaHalls() throws CinemaHallNotFoundException {
        List<HallEntity> hallEntities = hallRepository.showCinemaHalls();
        if(hallEntities.isEmpty()){
            throw new CinemaHallNotFoundException("Cannot find any cinema halls");
        }
        return hallEntities.stream().map(Hall::new).collect(Collectors.toList());
    }

    public List<Hall> showAvailableCinemaHalls(final Instant date) throws AvailableCinemaHallsNotFoundException {
        List<HallEntity> hallEntities = hallRepository.showAvailableCinemaHalls(date);
        if(hallEntities.isEmpty()){
            throw new AvailableCinemaHallsNotFoundException("Cannot find any cinema halls in this term");
        }
        return hallEntities.stream().map(Hall::new).collect(Collectors.toList());
    }

}
