package io.github.kkw.api.services;

import io.github.kkw.api.db.HallReservationRepository;
import io.github.kkw.api.db.PaymentsRepository;
import io.github.kkw.api.db.SeatReservationRepository;
import io.github.kkw.api.exceptions.ReservationAlreadyPaid;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;
import org.springframework.stereotype.Service;

@Service
public class PaymentsService {
    private final PaymentsRepository paymentsRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final HallReservationRepository hallReservationRepository;

    public PaymentsService(PaymentsRepository paymentsRepository,
                           SeatReservationRepository seatReservationRepository,
                           HallReservationRepository hallReservationRepository) {
        this.paymentsRepository = paymentsRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.hallReservationRepository = hallReservationRepository;
    }

    public double getPaymentAmountForSeat(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException, ReservationAlreadyPaid {
        if (seatReservationRepository.reservationDoesntExist(clientId.getId(), reservationId.getId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        if (paymentsRepository.isAlreadyPaidSeat(clientId.getId(), reservationId.getId())) {
            throw new ReservationAlreadyPaid("Reservation with this ID is already paid for");
        }
        return paymentsRepository.getPaymentAmountSeat(clientId.getId(), reservationId.getId());
    }

    public double getAdvancePaymentAmountForHall(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException, ReservationAlreadyPaid {
        if (hallReservationRepository.reservationDoesntExist(clientId.getId(), reservationId.getId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        if (paymentsRepository.isAlreadyPaidAdvanceHall(clientId.getId(), reservationId.getId())) {
            throw new ReservationAlreadyPaid("Advance payment for this reservation is already paid for");
        }
        return paymentsRepository.getPaymentAmountAdvanceHall(clientId.getId(), reservationId.getId());
    }

    public double getPaymentAmountForHall(ClientId clientId, ReservationId reservationId) throws ReservationNotFoundException, ReservationAlreadyPaid {
        if (hallReservationRepository.reservationDoesntExist(clientId.getId(), reservationId.getId())) {
            throw new ReservationNotFoundException("Reservation with this ID not found");
        }
        if (paymentsRepository.isAlreadyPaidHall(clientId.getId(), reservationId.getId())) {
            throw new ReservationAlreadyPaid("Reservation with this ID is already paid for");
        }
        double toPayTotal = paymentsRepository.getPaymentAmountHall(clientId.getId(), reservationId.getId());
        if (paymentsRepository.isAlreadyPaidAdvanceHall(clientId.getId(), reservationId.getId())) {
            toPayTotal -= paymentsRepository.getPaymentAmountAdvanceHall(clientId.getId(), reservationId.getId());
        }
        return toPayTotal;
    }

    public void finishPaymentSeat(ClientId clientId, ReservationId reservationId) {
        paymentsRepository.finishPaymentSeat(clientId.getId(), reservationId.getId());
    }

    public void finishAdvancePaymentHall(ClientId clientId, ReservationId reservationId) {
        paymentsRepository.finishAdvancePaymentHall(clientId.getId(), reservationId.getId());
    }

    public void finishPaymentHall(ClientId clientId, ReservationId reservationId) {
        paymentsRepository.finishPaymentHall(clientId.getId(), reservationId.getId());
    }
}