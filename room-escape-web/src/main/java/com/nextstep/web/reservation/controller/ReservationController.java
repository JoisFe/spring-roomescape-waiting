package com.nextstep.web.reservation.controller;

import com.nextstep.web.auth.LoginMemberPrincipal;
import com.nextstep.web.auth.UserDetail;
import com.nextstep.web.member.LoginMember;
import com.nextstep.web.reservation.app.ReservationCommandService;
import com.nextstep.web.reservation.app.ReservationQueryService;
import com.nextstep.web.reservation.dto.CreateReservationRequest;
import com.nextstep.web.reservation.dto.ReservationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationQueryService reservationQueryService;
    private final ReservationCommandService reservationCommandService;

    public ReservationController(ReservationQueryService reservationQueryService, ReservationCommandService reservationCommandService) {
        this.reservationQueryService = reservationQueryService;
        this.reservationCommandService = reservationCommandService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> read(@RequestParam String date) {
        return ResponseEntity.ok(reservationQueryService.findAllBy(date));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateReservationRequest request,
                                       @LoginMemberPrincipal UserDetail userDetail) {
        Long id = reservationCommandService.save(request, LoginMember.from(userDetail));
        return ResponseEntity.created(URI.create("/reservations/" + id)).build();
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id) {
        reservationCommandService.approve(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    private ResponseEntity<Void> cancel(@PathVariable Long id, @LoginMemberPrincipal LoginMember loginMember) {
        reservationCommandService.cancel(id, loginMember);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel-approve")
    private ResponseEntity<Void> cancelApprove(@PathVariable Long id, @LoginMemberPrincipal UserDetail userDetail) {
        reservationCommandService.approveCancel(id, LoginMember.from(userDetail));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> delete(@PathVariable Long id, @LoginMemberPrincipal UserDetail userDetail) {
        reservationCommandService.delete(id, LoginMember.from(userDetail));
        return ResponseEntity.noContent().build();
    }
}
