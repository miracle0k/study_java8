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
      // 람다 표현식은 지연 실행된다.
      info(Logger.getGlobal(), () -> "x: " + x + ", y: " + y);
   }
}
```
`() ->  "x: " + x + ", y: " + y`는 get()이 호출될 때 실행됨.

## 람다 표현식의 파라미터
```java
public class Parameters {
   public static void main(String[] args) {
      repeat(10, i -> System.out.println("Countdown: " + (9 - i)));
      repeat(10, () -> System.out.println("Hello, World!"));
   }
   // 파라미터를 받아서 특별한 처리할 때.
   public static void repeat(int n, IntConsumer action) {
      for (int i = 0; i < n; i++) {
         action.accept(i);
      }
   }
   // 특별히 파라미터가 필요 없을 때.
   public static void repeat(int n, Runnable action) {
      for (int i = 0; i < n; i++) {
         action.run();
      }
   }
}
```
IntConsumer는 몇번째 실행중인지 action에게 알려 줄 수 있으며, Runnable는 인자가 필요없을때 사용 가능.

각각의 상황에 맞도록 파리미터 선언이 필요!

## 함수형 인터페이스 선택

## 함수 리턴
## 합성
## 지연
## 연산 병렬화
## 예외 다루기
## 람다와 제네릭
## 모나드 연산