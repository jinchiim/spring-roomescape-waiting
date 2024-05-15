package roomescape.reservation.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.theme.domain.Theme;

@DataJpaTest
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("제한된 개수에 맞게 인기 많은 테마를 조회해 온다.")
    void findAllByDateOrderByThemeIdCountLimit() {
        LocalDate from = LocalDate.of(2024, 4, 28);
        LocalDate to = LocalDate.of(2024, 5, 28);
        List<Theme> themes = reservationRepository.findAllByDateOrderByThemeIdCountLimit(from, to, 1);

        assertAll(
                () -> assertEquals(themes.size(), 1),
                () -> assertEquals(themes.get(0).getId(), 1)
        );
    }
}
