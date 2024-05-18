package roomescape.theme.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.model.RoomEscapeException;
import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeExceptionCode;
import roomescape.time.domain.Time;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    public static final Theme THEME = Theme.of("미르", "미르 방탈출", "썸네일 Url");
    public static final Member MEMBER = Member.of("polla@gmail.com", "polla99");

    @InjectMocks
    private ThemeService themeService;
    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약이 존재하는 테마는 삭제하지 못한다.")
    void validateReservationExistence_ShouldThrowException_WhenReservationExist() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(Reservation.of(LocalDate.now().plusDays(1), Time.from(LocalTime.now()), THEME, MEMBER));

        when(reservationRepository.findByThemeId(1L))
                .thenReturn(reservations);

        Throwable reservationExistAtTime = assertThrows(
                RoomEscapeException.class,
                () -> themeService.removeTheme(1L));

        assertEquals(ThemeExceptionCode.USING_THEME_RESERVATION_EXIST.getMessage(),
                reservationExistAtTime.getMessage());
    }
}
