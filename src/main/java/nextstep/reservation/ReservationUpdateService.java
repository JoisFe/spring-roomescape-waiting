package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReservationUpdateService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationUpdateService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public Reservation cancelById(Long memberId, Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        Member member = memberDao.findById(memberId);
        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }
        reservation.cancel();
        reservationDao.updateStatus(reservation);
        return reservation;
    }

    public Reservation adminCancelById(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        reservation.cancelByAdmin();
        reservationDao.updateStatus(reservation);
        return reservation;
    }

    public Reservation cancelApproveById(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        reservation.cancelApproveByAdmin();
        reservationDao.updateStatus(reservation);
        return reservation;
    }

    public void approveById(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        reservation.approve();
        reservationDao.updateStatus(reservation);
    }
}
