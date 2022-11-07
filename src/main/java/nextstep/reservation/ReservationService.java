package nextstep.reservation;

import auth.AuthenticationException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.reservation_waiting.ReservationWaiting;
import nextstep.reservation_waiting.ReservationWaitingDao;
import nextstep.reservation_waiting.ReservationWaitingService;
import nextstep.sales.SalesService;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.support.DuplicateEntityException;
import nextstep.theme.Theme;
import nextstep.theme.ThemeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class ReservationService {
    public final ReservationDao reservationDao;
    public final ThemeDao themeDao;
    public final ScheduleDao scheduleDao;
    public final MemberDao memberDao;
    public final SalesService salesService;
    public final ReservationWaitingDao reservationWaitingDao;

    public ReservationService(
            ReservationDao reservationDao,
            ThemeDao themeDao,
            ScheduleDao scheduleDao,
            MemberDao memberDao,
            SalesService salesService,
            ReservationWaitingDao reservationWaitingDao
    ) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.scheduleDao = scheduleDao;
        this.memberDao = memberDao;
        this.salesService = salesService;
        this.reservationWaitingDao = reservationWaitingDao;
    }

    public Long create(Member member, ReservationRequest reservationRequest) {
        if (member == null) {
            throw new AuthenticationException();
        }
        Schedule schedule = scheduleDao.findById(reservationRequest.getScheduleId());
        if (schedule == null) {
            throw new NullPointerException();
        }

        List<Reservation> reservation = reservationDao.findByScheduleId(schedule.getId());
        if (!reservation.isEmpty()) {
            throw new DuplicateEntityException();
        }

        Reservation newReservation = new Reservation(schedule, member, ReservationStatus.PAYMENT_WAITING);
        return reservationDao.save(newReservation);
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {
        Theme theme = themeDao.findById(themeId);
        if (theme == null) {
            throw new NullPointerException();
        }

        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public void deleteById(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        reservationDao.deleteById(id);
    }

    public boolean existsReservationInSchedule(Long scheduleId) {
        return reservationDao.existReservationByScheduleId(scheduleId);
    }

    public List<Reservation> findAllByMember(Member member) {
        return reservationDao.findByMemberId(member.getId());
    }

    public void approveReservation(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        reservation.approve();
        reservationDao.update(reservation);
        salesService.createApproveSale(reservationId, reservation.getSchedule().getTheme().getPrice());
    }

    public void cancelReservation(Member member, Long id) {
        Reservation reservation = reservationDao.findById(id);
        if (reservation == null) {
            throw new NullPointerException();
        }

        if (!reservation.sameMember(member)) {
            throw new AuthenticationException();
        }

        if (reservation.isApprove()) {
            reservation.cancelRequest();
        }

        if (reservation.isPaymentWaiting()) {
            reservation.cancel();
        }

        reservationDao.update(reservation);
    }

    public void cancelApprove(Long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation == null) {
            throw new NullPointerException();
        }

        reservation.cancel();
        reservationDao.update(reservation);

        salesService.cancelSale(reservationId);

        ReservationWaiting reservationWaiting = reservationWaitingDao.findByScheduleId(reservation.getSchedule().getId());
        if (Objects.isNull(reservationWaiting)) {
            return;
        }
        Member member = memberDao.findById(reservationWaiting.getId());
        Reservation newReservation = Reservation.from(reservationWaiting, member, ReservationStatus.PAYMENT_WAITING);
        reservationDao.save(newReservation);
    }
}
