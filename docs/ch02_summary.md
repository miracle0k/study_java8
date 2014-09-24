# 01 반복에서 스트림 연산으로
스트림 특징

1. 스트림은 요소를 보관하지 않는다.
   요소들은 하부의 컬렉션에 보관되거나 필요시 생성.
2. 스트림 연산은 원본을 변경하지 않는다.
   새로운 스트림을 반환!
3. 스트림 연산은 가능하면 지연(Lazy) 처리된다.
   지연 처리란 결과가 필요하기 전에는 실행되지 않음.
   예를 들어, 긴 단어를 모두 세는 대신 처음 5개 긴 단어를 요청하면, filter 메서드는 5번 째 일치 후 필터링을 중단한다.
   결과적으로 심지어 무한 스트림(infinite stream)도 만들 수 있다!

스트림 연산 단계

1. 스트림을 생성한다.
2. 초기 스트림을 다른 스트림으로 변환하는 중간 연산(intermediate operation)들을 하나 이상의 단계로 지정한다.
3. 결과를 산출하기 위해 최종 연산(terminal operation)을 적용한다. 
이 연산은 앞선 지연 연산(lazy operation)들을 실행, 이후로는 해당 스트림은 사용 불가.

# 03 filter, map, flatMap 메서드
filter
* 특정 조건으로 스트림을 걸러낸다.
* 파라미터는 `Predicate<T>`로 리턴 값은 Boolean.

map
* 스트림을 특정 방식으로 변환한다.
* 변환을 수행하는 함수를 파라미터로 받음.

flatMap
* 스트림을 펼친다(?)

# 06 단순 리덕션
스트림으로 부터 어떤 값을 없는 행위, 최종 연산으로 리덕션 수행 후 해당 스트림은 사용 못함.
* max : 큰값.
* min : 작은값.
* findFirst : 첫값.
* findAny : 어떤 값이든지..
* anyMatch : 일치하는 값이 있는지.. 
* allMatch, noneMatch.. --> 전체 스트림을 검사하지만, 병렬 실행을 통해서 이점을 얻을 수 있음.

# 08 리덕션 연산

연산이 [결합 법칙](http://ko.wikipedia.org/wiki/%EA%B2%B0%ED%95%A9%EB%B2%95%EC%B9%99)이 성립하면, reduce 메소드로 리덕션 연산을 할 수 있으며, 병렬 스트림을 통하여 효율적으로 리덕션 연산을 수행 할 수 있다.

# 09 결과 모으기
반복자
* iterator로 반복자 얻을 수 있음.
* toArray로 배열 얻을 수 있고, 특정 타입의 배열 생성 위해서는 배열의 생성자를 넘겨줘야함.

collect 등장
* HashSet에 결과를 저장시 HashSet은 쓰레드에 안전하지 않므로 스트림을 병렬 처리 불가.
* reduce 사용 못하고, collect 사용해야 함.

collect의 인자

1. supplier(공급자) : 대상 객체의 인스턴스 생성.
2. accumulator(누산자) : 요소를 대상에 추가.
3. combiner(결합자) : 두 객체를 하나로 병합.

```java
HashSet<String> result = stream.collect(HashSet::new, HashSet::add, HashSet::add);
```
병렬 처리시 각각의 쓰레드가 HashSet을 만들고, 나중에 하나의 객체로 합친다.

Collectors : 자주 사용하는 collect를 모아둔 곳.
* List -> `stream.collect(Collectors.toList());`
* Set -> `stream.collect(Collectors.toSet());`
* 특정 컬렉션 -> `stream.collect(Collectors.toCollection(TreeSet::new));`
* 문자열 연결 -> `stream.collect(Collectors.join(", "));`
* 요약 -> `stream.collect(Collectors.summarizingInt(String::length));`
```java
IntSummaryStatistics summary = words.stream().collect(Collectors.summarizingInt(String::length));
assertThat(summary.getCount(), is(9989L));
assertThat(summary.getAverage(), is(3.9401341475623184D));
assertThat(summary.getMax(), is(14));
```

# 10 맵으로 모으기