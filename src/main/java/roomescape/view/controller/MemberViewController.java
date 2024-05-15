package roomescape.view.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberViewController {

    @GetMapping("/member/reservation")
    public String memberReservationPage() {
        return "reservation-mine";
    }
}