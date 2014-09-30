# 3장 람다를 이용한 프로그래밍
## 지연 실행
모든 람다의 핵심 : 지연 실행

지연실행 이유
* 별도의 스레드에서 코드 실행
* 코드를 여러번 실행
* 코드를 적절한 시점에서 실행
* 특정 이벤트 발생시 코드 실행 (버튼 클릭, 데이터 전송 완료)
* 필요시에만 실행

예제) 로그를 남겨야할 때만 코드 실행.
```java
public class Logging {
   public static void info(Logger logger, Supplier<String> message) {
      if (logger.isLoggable(Level.INFO))
         logger.info(message.get());
   }
   public static void main(String[] args) {
      double x = 3;
      double y = 4;
      info(Logger.getGlobal(), () -> "x: " + x + ", y: " + y);
   }
}
```
`() ->  "x: " + x + ", y: " + y`는 get()이 호출될 때 실행됨.

## 람다 표현식의 파라미터

## 함수형 인터페이스 선택
## 함수 리턴
## 합성
## 지연
## 연산 병렬화
## 예외 다루기
## 람다와 제네릭
## 모나드 연산