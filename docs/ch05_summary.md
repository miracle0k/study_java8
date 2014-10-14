# 타임라인
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

# 지역 날짜
# 날짜 조정기
# 지역 시간
# 구역 시간
# 포멧팅과 파싱
# 레거시 코드와 상호 동작