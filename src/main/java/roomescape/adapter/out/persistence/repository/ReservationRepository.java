package roomescape.adapter.out.persistence.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.adapter.out.ReservationEntity;
import roomescape.adapter.out.ReservationTimeEntity;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

  Integer countById(Long id);

  Optional<ReservationEntity> findByReservationTime(ReservationTimeEntity reservationTimeEntity);
}
