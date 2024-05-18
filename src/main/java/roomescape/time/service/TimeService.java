package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.Time;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.exception.TimeExceptionCode;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public TimeResponse addReservationTime(TimeRequest timeRequest) {
        validateDuplicateTime(timeRequest.startAt());
        Time reservationTime = Time.from(timeRequest.startAt());
        Time savedReservationTime = timeRepository.save(reservationTime);

        return toResponse(savedReservationTime);
    }

    public List<TimeResponse> findReservationTimes() {
        List<Time> reservationTimes = timeRepository.findAllByOrderByStartAt();

        return reservationTimes.stream()
                .map(this::toResponse)
                .toList();
    }

    public void removeReservationTime(long reservationTimeId) {
        validateReservationExistence(reservationTimeId);
        timeRepository.deleteById(reservationTimeId);
    }

    public TimeResponse toResponse(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt());
    }

    public void validateDuplicateTime(LocalTime startAt) {
        Optional<Time> time = timeRepository.findByStartAt(startAt);

        if (time.isPresent()) {
            throw new RoomEscapeException(TimeExceptionCode.DUPLICATE_TIME_EXCEPTION);
        }
    }

    public void validateReservationExistence(long timeId) {
        List<Reservation> reservation = reservationRepository.findByTimeId(timeId);

        if (!reservation.isEmpty()) {
            throw new RoomEscapeException(TimeExceptionCode.EXIST_RESERVATION_AT_CHOOSE_TIME);
        }
    }
}
