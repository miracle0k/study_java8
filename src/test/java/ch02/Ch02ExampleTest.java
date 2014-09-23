package ch02;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Ch02ExampleTest {
    private List<String> words;
    private String contents;

    @Before public void setUp() throws Exception{
        ClassLoader loader = Ch02ExampleTest.class.getClassLoader();
        String classPathRoot = loader.getResource(".").getPath();
        contents = new String(Files.readAllBytes(Paths.get(classPathRoot, "ch02/alice.txt")), StandardCharsets.UTF_8);
        words = Arrays.asList(contents.split("[\\P{L}]+"));
    }

    @Test public void _01_단순반복() {
        int count = 0;
        for (String w : words) {
            if (w.length() > 12) {
                count++;
            }
        }

        assertThat(count, is(9));
    }

    @Test public void _01_스트림() {
        long count = words.stream().filter(w -> w.length() > 12).count();

        assertThat(count, is(9L));
    }

    @Test public void _01_병렬스트림() {
        long count = words.parallelStream().filter(w -> w.length() > 12).count();

        assertThat(count, is(9L));
    }

    @Test public void _02_스트림_생성() {
        // 배열로 부터~
        Stream<String> words = Stream.of(contents.split("[\\P{L}]+"));

        // 가변 인자로..
        Stream<String> song = Stream.of("gently", "down", "the", "stream");

        // 빈 스트림
        Stream<String> silence = Stream.empty();
    }

    @Test public void _02_무한_스트림_생성() {
        // 상수 무한 스트림
        Stream<String> echos = Stream.generate(() -> "Echo");
        // 난수 무한 스트림
        Stream<Double> randoms = Stream.generate(Math::random);
        // 무한 수열 스트림
        Stream.iterate(BigInteger.ZERO, n -> n.add(BigInteger.ONE));
    }
    @Test public void _03_filter() {
        Stream<String> longWords = words.stream().filter(w -> w.length() > 12);

        Optional<String> first = longWords.findFirst();
        assertThat(first.isPresent(), is(true));
        assertThat(first.get(), is("conversations"));
    }

    @Test public void _03_map() {
        Stream<String> lowercaseWords = words.stream().map(String::toLowerCase);

        assertFalse("대문자를 포함하고 있으면 안됨.",lowercaseWords.allMatch(w -> w.matches("[A-Z]+")));
    }

    @Test public void _03_map2() {
        Stream<Character> firstChars = words.stream().map(s -> s.charAt(0));
    }

    public static Stream<Character> characterStream(String s) {
        List<Character> result = new ArrayList<>();
        for (char c : s.toCharArray()) {
            result.add(c);
        }
        return result.stream();
    }
    @Test public void _03_flatMap() {
        Stream<Stream<Character>> result = words.stream().map(w -> characterStream(w));

        // characterStream는 Stream<Stream<Character>>을 돌려주는데,
        // 이걸 한번 더 풀어서 Stream<Character>로 반환.
        Stream<Character> letters = words.stream().flatMap(w -> characterStream(w));
    }

    @Test public void _04_서브스트림_추출() {
        // limit
        Stream<Double> randoms = Stream.generate(Math::random).limit(100);

        assertThat(randoms.count(), is(100L));

        // skip
        Stream<String> words = Stream.of(contents.split("[\\P{L}]+")).skip(1);
        assertThat(words.findFirst().get(), is("was"));
    }

    @Test public void _04_스트림_결합() {
        Stream<Character> combined = Stream.concat(characterStream("Hello"), characterStream("World"));

        assertThat(combined.count(), is(10L));
    }

    @Test public void _05_상태유지변환_StatefulTransformation() {
        // distinct는 이전 값을 기억하고 있어야 한다.
        Stream<String> uniqueWords = Stream.of("merrily", "merrily", "merrily", "gently", "merrily").distinct();

        final List<String> actual = uniqueWords.collect(Collectors.toList());
        assertThat(actual.get(0), is("merrily"));
        assertThat(actual.get(1), is("gently"));

        // sort도 이전 값을 기억하고 있어야한다. (전체 값을 다 알고 있어야..)
        Stream<String> longestFirst = words.stream().sorted(Comparator.comparing(String::length).reversed());
        final Optional<String> longestFirstFirst = longestFirst.findFirst();
        assertThat(longestFirstFirst.get(), is("disappointment"));
    }

    @Test public void _06_단순리덕션() {
        Optional<String> largest = words.stream().max(String::compareToIgnoreCase);
        if (largest.isPresent()) {
            System.out.println("큰값: " + largest.get());
        } else {
            System.out.println("Empty~");
        }
    }
}