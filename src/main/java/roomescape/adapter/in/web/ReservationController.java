package roomescape.adapter.in.web;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.annotation.UserInfo;
import roomescape.application.dto.ReservationCommand;
import roomescape.application.dto.ReservationMineResponse;
import roomescape.application.dto.ReservationResponse;
import roomescape.application.port.in.ReservationUseCase;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

  private final ReservationUseCase reservationUseCase;

  public ReservationController(ReservationUseCase reservationUseCase) {
    this.reservationUseCase = reservationUseCase;
  }

  @GetMapping
  public ResponseEntity<List<ReservationResponse>> getReservations() {
    return new ResponseEntity<>(reservationUseCase.retrieveReservations(), HttpStatus.OK);

  }

  @PostMapping
  public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCommand reservationCommand) {
    ReservationResponse response = reservationUseCase.registerReservation(reservationCommand);

    System.out.println(response.toString());
    return ResponseEntity.status(OK)
                         .body(response);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  public void deleteReservation(@PathVariable("id") Long id) {
    reservationUseCase.deleteReservation(id);
  }

  @GetMapping("-mine")
  public ResponseEntity<List<ReservationMineResponse>> getMyReservations(@UserInfo Long memberId) {
    return new ResponseEntity<>(reservationUseCase.retrieveMyReservations(memberId), HttpStatus.OK);
  }
}
