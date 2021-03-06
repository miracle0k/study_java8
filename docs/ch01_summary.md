### 왜 람다인가?
람다 표현식 -> 한번 이상 실행하기 위해서 전달하는 코드블럭.

````java
class Worker implements Runnable {
    public void run() {
       // do something.
    }
}

Worker w = new Worker();
new Thead(w).start();
````
run 메소드가 쓰레드에서 실행하려는 코드를 담고 있다.


````java
Arrays.sort(words, new Comparator<String>() {
    @Override
    public int compare(String first, String second) {
        if (first.length() < second.length()) {
            return -1;
        } else if (first.length() > second.length()) {
            return 1;
        } else {
            return 0;
        }
    }
});
````
sort 메소드에 정령하기 위한 순서를 정하는 코드를 전달하면, 정렬된다.

````java
button.setOnAction(new EventHandler() {
    public void handle(Action event) {
        // do something.
    }
});
````
익명 클래스 형태의 콜백 액션 구현 방법. 중요한 부분은 handle내의 코드.

자바에서 코드 블록을 전달하는 일이 쉽지 않음.
* 객체지향 언어이기 때문에 단순히 코드만 전달하는 것이 아니라 클래스의 객체를 전달해야 함.

### 람다 표현식 문법
````java
(first, second) -> Integer.compare(first.length(), second.length())
````
람다 표현식은 단순한 코드 블록으로, 변수들의 명세와 함께 코드에 전달.

````java
//람다 표현식(블록)
(String first, String second) -> {
    if (first.length() < second.length()) {
        return -1;
    } else if (first.length() > second.length()) {
        return 1;
    } else {
        return 0;
    }
}

//파라미터 없을 때
() -> System.out.println("Hello~")

//파라미터 타입 추정 가능하면, 타입 생략 가능.
Comparator<String> comp = (first, second) -> Integer.compare(first.length(), second.length()));

// 메서드에서 추정되는 타입 한 개를 파라미터로 받으면 괄호 생략 가능
EventHandler<ActionEvent> listener = event -> System.out.println("Click~");
````

### 함수형 인터페이스
단일 추상 메소드(single abstract method)를 갖춘 인터페이스의 객체에서 람다 표현식을 사용할 수 있으며, 이런 인터페이스를 *함수형 인터페이스(functional interface)*라고 한다.

예) Comparator 인터페이스를 메소르를 한개만 가지고 있으므로 함수형 인터페이스이다.

````java
Arrays.sort(words, (first, second) -> Integer.compare(first.length(), second.length()));
````
* Arrays의 sortt 메소드는 Comparator<String> 인터페이스를 구현한 클래스가 필요.
* Comparator 인터페이스의 compare 메소드가 호출되면 람다 표현식이 실행 됨.

````java
//함수형 인터페이스
BiFunction<String, String, Integer> comp = (f, s) -> Integer.compare(f.length(), s.length());

Arrays.sort(words, comp); // 대입이 안되어서 오류 발생.
````
* 자바는 다른 함수형 언어와 달리 함수 리터럴(function literal)을 지원하지 않음.
* BiFunction 함수형 인터페이스를 받는 Array.sort가 없으므로 오류 발생.

````java
Runnable sleeper = () -> {
    System.out.println("Zzz");
    System.out.println("Zzz2");
    Thread.sleep(1000); // Checked Exception을 던지나 run 메소드는 예외를 던지지 않아서 오류.
};
````
* 검사 예외(checked exception)은 함수 인터페이스에 선언되어 있어야 던져질 수 있음.
    * Thead.sleep이 던지는 예외를 catch하거나, Checked Exception을 던질 수 있는 Callable를 이용해야 함.
 
### 메서드 레퍼런스
````java
// 메소드 레퍼런스
Runnable sleepr2 = System.out::println; // () -> System.out.println();
Math::pow // (x,y) -> Math.pow(x,y)
Arrays.sort(strings, String::compareToIgnoreCase) // (x, y) -> x.compareToIgnoreCase(y)
````

### 생성자 레퍼런스
````java
// 생성자 레퍼런스
List<String> lables = new ArrayList<>();
Stream<Button> stream = lables.stream().map(Button::new);
List<Button> buttons = stream.collect(Collectors.toList());
````

### 변수 유효 범위
람다 표현식의 구조

1. 코드 블록
2. 파라미터
3. 자유변수(파라미터도 아니고 코드 내부에도 정의되지 않은 변수)의 값

````java
public static void repeatMessage(String text, int count) {
    Runnable r = () -> {
        for (int i = 0; i < count; i++) {
            System.out.println(text);
            Thread.yield();
        }

        count++;// 오류 : 캡처된 변수는 final과 동일하게 취급.
    };
    new Thread(r).start();
}
repeatMessage("Hello", 1000);

````
* 위의 예제의 람다 표현식은 자유 변수 2개(text, count)를 포함함.
* 람다 표현식의 자료 구조는 해당 변수의 값(hello, 1000)을 저장해야 하며, 캡처(capture) 했다고 표현.
* 캡쳐된 변수는 변경 할 수 없음(final).
    * 쓰레드 세이프를 보장하기 위해서~

### 디폴트 메서드
````java
public interface Person {
    long getId();
    // 디폴트 메소드
    default String getName() {
        return "John Q. Public";
    }
}
````

1. 슈퍼 클래스가 우선한다. 슈퍼클래스에서 구체적인 메서드를 제공하는 경우, 이와 이름이 
2. 인터페이스들이 출동한다.어떤 슈퍼 인터페이스에서 디폴트 메소드를 제공하고, 또 다른 인터페이스에서
(디폴트 메서드든 아니든) 이름 및 파라미터 타입이 같은 메서드를 제공하는 경우에는 해당 메서드를
오버라이드해서 충돌을 해결해야 한다.

````java
interface Named {
    default String getName() {
        return "Name....";
    }
}

class Student implements Person, Named {

}
````
* Person, Named에 getName() 디폴트 메서드가 존재하여서, 컴파일 오류.
* 프로그래머가 모호함을 해결해야 함.

````java
class Student implements Person, Named {
  public String getName() {
    return Person.super.getName();
  }
}
````
* 어떤 getName()을 사용할지 지정..

````java
class Student extends Person implements Named {

}
````
* 슈퍼 클래스의 메서드만 중요!
* Named의 디폴트 메서드는 무시됨
* 즉, Person의 getName이 상속됨.

### 인터페이스의 정적 메서드
* Java 8부터 인터페이스에 static method를 추가할 수 있음.
* Collection/Collections, Path/Paths 같은 인터페이스와 유틸클래스를 같이두는 패턴은
이제 사용할 필요가 없음.

````java
public interface Path {
    public static Path get(String first, String... more) {
        return FileSystems.getDefault().getPath(first, more);
    }
}
````
* Path 인터페이스에 정적 메소드를 추가하면 Paths 클래스 필요 없음.
* Collection 인터페이스에 nCopies 정적 메소드를 추가하면...
    * Collections.nCopies 대신에 List.nCopies를 사용할 수 있다.
    * 코드를 읽을 때 반환 값이 List임을 바로 알 수 있음.
* 모든 Collection 위 방식으로 리펙토링될 것 같진 않음.
    * 인터페이스 직접 설계시 이를 이용하면 유용할 듯.

````java
Arrays.sort(words, Comparator.comparing(String::length));
````
* 위의 Comparator는 comparing 정적 메소드를 이용하면, 더 간단하게 구현 가능.