## 타임라인
자바의 시간 척도(time scale)
* 하루는 86400초.
* 정오에 공식 시간과 동기화.
* 그 외에는 정확하게 정의된 방식으로 공식 시간과 가깝게 조정.

```java
Instant start = Instant.now();
runAlgorithm();
Instant end = Instant.now();

Duration timeElapsed = Duration.between(start, end);
long millis = timeElapsed.toMillis();
System.out.printf("%d milliseconds\n", millis);
```
Instant - 시간상의 한 시점.
Duration - 두 인스턴스 사이의 시간.

## LocalDate
LocalDate로 날짜 표현(타임존 없음).

```java
LocalDate today = LocalDate.now();
System.out.println(today); // 오늘 날짜만 출력 됨!

LocalDate birthday = LocalDate.of(1982, 2, 28);
assertThat(birthday.toString(), is("1982-02-28"));

birthday = LocalDate.of(1982, Month.FEBRUARY, 28);
assertThat(birthday.toString(), is("1982-02-28"));
```

plusDays, until등을 이용해서 날짜 계산 가능.

## TemporalAdjusters
- TemporalAdjusters를 이용해서 특정 조건의 날짜를 얻을 수 있다. 예를 들면 '매월 첫 번째 화요일' 같은~

```java
LocalDate firstTuesday = LocalDate.of(year, month, 1)
	.with(TemporalAdjusters.nextOrSame(DayOfWeek.TUESDAY));
System.out.println("firstTuesday: " + firstTuesday);
```
- nextOrSame : 주어진 날짜부터 지정 요일에 해당하는 다음 또는 이전 날짜.
- 좀 더 자세한 메소드 설명은 교제 참고!(141페이지)

## LocalTime
LocalTime은 15:30:00 같은 시간을 표현.(타임존 없음)

```java
LocalTime rightNow = LocalTime.now();
LocalTime bedtime = LocalTime.of(22, 30);
bedtime = LocalTime.of(22, 30, 0);

System.out.println("rightNow: " + rightNow);
System.out.println("bedtime: " + bedtime);

LocalTime wakeup = bedtime.plusHours(8); // wakeup is 6:30
System.out.println("wakeup: " + wakeup);
```

- LocalDateTime은 날짜/시간 표현.(타임존 없음)
- 서머 타임이나 서로 다른 시간대에 사용자가 있는 경우 등, 타임존이 필요한 경우에는 다음에 설명할 ZonedTimeDate 사용해야함.

## ZonedTimeDate
## 포멧팅과 파싱
## 레거시 코드와 상호 동작