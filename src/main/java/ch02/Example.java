package ch02;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Created by miracle on 2014. 9. 21..
 */
public class Example {
    public static void main(String[] args) {
        // 스트림 생성
        Stream<String> song = Stream.of("gently", "down", "the", "stream");

        // 난수 100개
        Stream<Double> randoms = Stream.generate(Math::random).limit(100);

        //
        Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gentyl").distinct();

        Stream<Double> greatFirst = randoms.sorted(Comparator.comparing(Double::doubleValue).reversed());
        greatFirst.forEach(System.out::println);
    }
}