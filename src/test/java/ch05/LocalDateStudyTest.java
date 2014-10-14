package ch05;

import org.junit.Test;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by naver on 2014. 10. 14..
 */
public class LocalDateStudyTest {
    @Test
    public void 인스턴스_생성() {
        LocalDate today = LocalDate.now();
        System.out.println(today); // 오늘 날짜만 출력 됨!

        LocalDate birthday = LocalDate.of(1982, 2, 28);
        assertThat(birthday.toString(), is("1982-02-28"));

        birthday = LocalDate.of(1982, Month.FEBRUARY, 28);
        assertThat(birthday.toString(), is("1982-02-28"));
    }

    @Test
    public void 프로그래머의_날_계산() {
        final LocalDate programmersDay = LocalDate.of(2014, 1, 1).plusDays(255);

        assertThat(programmersDay, is(LocalDate.of(2014, 9, 13)));
    }

    @Test
    public void 남은날짜계산() {
        LocalDate independenceDay = LocalDate.of(2014, Month.JULY, 4);
        LocalDate christmas = LocalDate.of(2014, Month.DECEMBER, 25);

        System.out.println("Until christmas: " + independenceDay.until(christmas));
        System.out.println("Until christmas: " + independenceDay.until(christmas, ChronoUnit.DAYS));
    }

    @Test
    public void 윤년에서날짜계산() {
        assertThat(LocalDate.of(2016, 1, 31).plusMonths(1), is(LocalDate.of(2016, 2, 29)));
        assertThat(LocalDate.of(2016, 3, 31).minusMonths(1), is(LocalDate.of(2016, 2, 29)));
    }

    @Test(expected = DateTimeException.class)
    public void 엉뚱한날짜로생성시() {
        assertThat(LocalDate.of(2014, 2, 29), is(LocalDate.of(2014, 2, 28)));
    }

    @Test
    public void dayOfWeek() {
        DayOfWeek startOfLastMillennium = LocalDate.of(1900, 1, 1).getDayOfWeek();
        System.out.println("startOfLastMillennium: " + startOfLastMillennium);
        System.out.println(startOfLastMillennium.getValue());
        System.out.println(DayOfWeek.SATURDAY.plus(3));
    }
}
