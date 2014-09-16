## 왜 람다인가?
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

## 람다 표현식 문법

