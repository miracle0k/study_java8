package ch01;

import javafx.scene.control.Button;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Example {
    public static void main(String[] args) {
        String[] words = {"abcd", "a"};

        // 익명 클래스
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

        //람다 표현식(블록)
        Arrays.sort(words, (first, second) -> {
            if (first.length() < second.length()) {
                return -1;
            } else if (first.length() > second.length()) {
                return 1;
            } else {
                return 0;
            }
        });

        //람다 표현식
        Arrays.sort(words, (first, second) -> Integer.compare(first.length(), second.length()));

        System.out.println(words[0]);

        //함수형 인터페이스
        BiFunction<String, String, Integer> comp = (f, s) -> Integer.compare(f.length(), s.length());

        // 대입 안됨.
        //Arrays.sort(words, comp);

        // Checked Excption은 해당 Excption이 대상 인터페이스의 추상 메서드에 선언되어 있어야함.
        // Runnable.run 메소드는 예외를 던질 수 없음.
        Runnable sleeper = () -> {
            System.out.println("Zzz");
            System.out.println("Zzz2");
            // 오류 Thread.sleep(1000);
        };
        Thread t = new Thread(sleeper);
        t.run();

        // 메소드 레퍼런스
        Runnable sleepr2 = System.out::println; // == () -> System.out.println();

        // 생성자 레퍼런스
        List<String> lables = new ArrayList<>();
        Stream<Button> stream = lables.stream().map(Button::new);
        List<Button> buttons = stream.collect(Collectors.toList());

    }

    // 변수유효 범위
    public static void repeatMessage(String text, int count) {
        Runnable r = () -> {
            for (int i = 0; i < count; i++) {
                System.out.println(text);
                Thread.yield();
            }
            // 캡처된 변수는 final과 동일하게 취급.
            // count++;
        };
        new Thread(r).start();
    }
}