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
LocalDate로 날짜 표현(타임존은 무시).

```java
LocalDate today = LocalDate.now();
System.out.println(today); // 오늘 날짜만 출력 됨!

LocalDate birthday = LocalDate.of(1982, 2, 28);
assertThat(birthday.toString(), is("1982-02-28"));

birthday = LocalDate.of(1982, Month.FEBRUARY, 28);
assertThat(birthday.toString(), is("1982-02-28"));
```

plusDays, until등을 이용해서 날짜 계산 가능.

## 날짜 조정기
## 지역 시간
## 구역 시간
## 포멧팅과 파싱
## 레거시 코드와 상호 동작