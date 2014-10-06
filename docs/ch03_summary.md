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
IntConsumer는 몇번째 실행중인지 action에게 알려 줄 수 있으며, Runnable는 인자가 필요없을때 사용 가능하다.

## 함수형 인터페이스 선택
대부분의 함수형 언어에서는 함수 타입으로 함수를 표현
` (String, String) -> int`, `Function2<String, String, Integer>`

자바에서는 `Comparator<String>`같은 함수형 인터페이스로 함수의 의도를 표현.
프로그래밍 언어 이론에서는 **명목적 타이핑(nominal typing or name-based type system)**이라 한다.

참고 : [Nominative and Structural type systems](http://en.wikipedia.org/wiki/Nominative_and_structural_type_systems)

공통 함수형 인터페이스
* Runnalbe, Suppliter, Consumer, BiConsumer, Function, BiFunction, UnaryOperator, BinaryOperator, Predicate, BiPredicate.
* 자세한 설명은 교제 76쪽 표 3-1 참고

예) 표준 함수형 인터페이스 사용한 메서드
```java
public static Image transform(Image in, UnaryOperator<Color> f) {
  int width = (int) in.getWidth();
  int height = (int) in.getHeight();
  WritableImage out = new WritableImage(
     width, height);
  for (int x = 0; x < width; x++)
     for (int y = 0; y < height; y++)
        out.getPixelWriter().setColor(x, y,
           f.apply(in.getPixelReader().getColor(x, y)));
  return out;
}

//호출예
Image brightenedImage = transform(image, Color::brighter);
```

자바 기본 타입용으로 32가지 함수형 인터페이스를 제공하며, 이를 통하여 오토박싱을 피할 수 있다.
* 자세한 내용은 교제 78쪽 표 3-2 참고.

함수 인터페이스 정의
```java
@FunctionalInterface
interface ColorTransformer {
   Color apply(int x, int y, Color colorAtXY);
}
```
추상메서드 이름이 apply인 이유는 함수형 인터페이스의 대부분이 apply를 사용하기 때문. 다른 이름을 써도 괜찮으나 표준 이름을 고수하면 의미 전달이 쉽다(?)

## 함수 리턴
함수형 언어에서는 함수가 일차 구성원으로 함수를 메서드의 인자나 리턴값으로 사용할 수 있다.
자바는 함수형 인터페이스를 인자나 리턴값으로 사용하여 함수(?)를 전달 할 수 있다.

예) 함수형 인터페이스를 리턴하는 메소드
```java
public static Image transform(Image in, UnaryOperator<Color> f) {
   //do something...
}

public static UnaryOperator<Color> brighten(double factor) {
  return c -> c.deriveColor(0, 1, factor, 1);
}

// call example
Image brightenedImage = transform(image, brighten(1.2));
```
brighten 메소드는 UnaryOperator 함수형 인터페이스를 리턴하며, 리턴값을 transform의 인자로 사용할 수 있다.

## 합성
예) 이미지를 밝게 -> 흑백으로 전환.
```java
Image image = new Image("eiffel-tower.jpg");
Image image2 = transform(image, Color::brighter);
Image image3 = transform(image2, Color::grayscale);
```
이미지를 밝게한 후에 흑백으로 전환시 중간 이미지가 필요하며, 만일 큰 그림이라면 많은 리소스가 필요하다.
각각의 연산을 합성해서 픽셀 단위로 적용 할 수 있으면 좋음.

예) 합성 적용
```java
public static <T> UnaryOperator<T> compose(UnaryOperator<T> op1,
  UnaryOperator<T> op2) {
  return t -> op2.apply(op1.apply(t));
}

// 호출 예
transform(image, compose(Color::brighter, Color::grayscale))
```
compose에 의해서 합성된 연산이 각각의 픽셀에 적용되며, 중간 이미지도 필요 없음.

## 지연
지연처리시에는 수행할 작업을 쌓아두는 중간 연산과 결과를 주는 최종 연산을 구분해야 함.

예) 지연 처리
```java
class LatentImage {
   private Image in;
   private List<UnaryOperator<Color>> pendingOperations;

   public static LatentImage from(Image in) {
      LatentImage result = new LatentImage();
      result.in = in;
      result.pendingOperations = new ArrayList<>();
      return result;
   }
}
```
별도의 LatentImage에서 지연될 이미지 연산을 저장한다. Image에 메서드를 추가할 수 없으므로 스트림과 비슷한 from 팩토리 메소드를 제공함.

예) 중간 연산
```java
LatentImage transform(UnaryOperator<Color> f) {
  pendingOperations.add(f);
  return this;
}
```
지연 처리될 연산을 저장하는 중간 연산인 transform 제공.

예) 최종 연산
```java
public Image toImage() {
  int width = (int) in.getWidth();
  int height = (int) in.getHeight();
  WritableImage out = new WritableImage(width, height);
  for (int x = 0; x < width; x++)
     for (int y = 0; y < height; y++) {
        Color c = in.getPixelReader().getColor(x, y);
        for (UnaryOperator<Color> f : pendingOperations) c = f.apply(c);
        out.getPixelWriter().setColor(x, y, c);
     }
  return out;
}
```
연산 결과 리턴하는 최종 연산인 toImage를 제공한다. toImage가 호출 될때까지 잠시 연산을 지연 시킬 수 있다.

모든 연산에 지연 연산을 적용할 수 업속, 실제 연산들이 복잡하기 때문에 지연 연산을 구현하기는 무척 어려운 작업임.

## 연산 병렬화
연산을 쪼깨서 동시에 수행할 수 있는지 검토해보고, 적용시 장단점을 고려하여 병렬 처리를 할지 결정 해야한다.

예) transform 병렬 버전 
```java
   public static Color[][] parallelTransform(Color[][] in, UnaryOperator<Color> f) {
      int n = Runtime.getRuntime().availableProcessors();
      int height = in.length;
      int width = in[0].length;
      Color[][] out = new Color[height][width];
      try {
         ExecutorService pool = Executors.newCachedThreadPool();
         for (int i = 0; i < n; i++) {
            int fromY = i * height / n;
            int toY = (i + 1) * height / n;
            pool.submit(() -> {
                  System.out.printf("%s %d...%d\n", Thread.currentThread(), fromY, toY - 1);
                  for (int x = 0; x < width; x++)
                     for (int y = fromY; y < toY; y++)
                        out[y][x] = f.apply(in[y][x]);
               });
         }
         pool.shutdown();
         pool.awaitTermination(1, TimeUnit.HOURS);
      }
      catch (InterruptedException ex) {
         ex.printStackTrace();
      }
      return out;
   }
```
## 예외 다루기

## 람다와 제네릭
* 읽기 -> 공변(covariant[서브타입도 사용 가능])
* 쓰기 -> 역변(contravariant[슈퍼타입도 사용 가능])
* 인자 타입에는 super를 사용
* 리턴 타입에는 extends를 사용

```java
Stream<T> filter(Predicate<? super T> predicate)
<R> Stream<R> map(Function<? super T, ? extends R> mapper)
```

## 모나드 연산
무슨 이야기인지 모르겠다.. ㅋ