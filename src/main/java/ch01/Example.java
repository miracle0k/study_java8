/*
 * @(#)Example.class  2014. 9. 12..
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ch01;

import javafx.scene.control.Button;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
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

        //함수형 인터페이스
        BiFunction<String, String, Integer> comp = (f,s) -> Integer.compare(f.length(), s.length());

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

        System.out.println(words[0]);

        Runnable sleepr2 = System.out::println; // == () -> System.out.println();
    }
}