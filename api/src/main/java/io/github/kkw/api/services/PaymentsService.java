package io.github.kkw.api.services;

import io.github.kkw.api.exceptions.ReservationAlreadyPaid;
import io.github.kkw.api.exceptions.ReservationNotFoundException;
import io.github.kkw.api.model.ClientId;
import io.github.kkw.api.model.ReservationId;

public interface PaymentsService {
    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of seat reservation to get payment amount for
     * @return amount to pay for seat
     * @throws ReservationNotFoundException reservation with this ID not found
     * @throws ReservationAlreadyPaid reservation with this ID was already paid for
     */
    double getPaymentAmountForSeat(ClientId clientId, ReservationId reservationId)
     throws ReservationNotFoundException, ReservationAlreadyPaid;

    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of hall reservation to get advance payment amount for
     * @return amount to pay in advance for hall
     * @throws ReservationNotFoundException reservation with this ID not found
     * @throws ReservationAlreadyPaid reservation with this ID was already paid for
     */
    double getAdvancePaymentAmountForHall(ClientId clientId, ReservationId reservationId)
     throws ReservationNotFoundException, ReservationAlreadyPaid;

    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of hall reservation to get total payment amount for
     * @return amount to pay in total for hall
     * @throws ReservationNotFoundException reservation with this ID not found
     * @throws ReservationAlreadyPaid reservation with this ID was already paid for
     */
    double getPaymentAmountForHall(ClientId clientId, ReservationId reservationId)
     throws ReservationNotFoundException, ReservationAlreadyPaid;

    // below methods are used by payments mock, should not be used directly
    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of seat reservation to finish payment for
     */
    void finishPaymentSeat(ClientId clientId, ReservationId reservationId);

    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of hall reservation to finish advance payment for
     */
    void finishAdvancePaymentHall(ClientId clientId, ReservationId reservationId);

    /**
     *
     * @param clientId ID of calling client
     * @param reservationId ID of hall reservation to finish payment for
     */
    void finishPaymentHall(ClientId clientId, ReservationId reservationId);
}
