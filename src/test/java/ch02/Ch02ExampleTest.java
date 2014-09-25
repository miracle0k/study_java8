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

public class Ch02ExampleTest {
    private List<String> words;
    private String contents;
    private Integer[] digits;

    @Before public void setUp() throws Exception{
        ClassLoader loader = Ch02ExampleTest.class.getClassLoader();
        String classPathRoot = loader.getResource(".").getPath();
        contents = new String(Files.readAllBytes(Paths.get(classPathRoot, "ch02/alice.txt")), StandardCharsets.UTF_8);
        words = Arrays.asList(contents.split("[\\P{L}]+"));

        digits = new Integer[]{
                3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6,
                4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9,
                3, 7, 5, 1, 0, 5, 8, 2, 0, 9, 7, 4, 9, 4, 4, 5, 9, 2, 3, 0, 7, 8, 1,
                6, 4, 0, 6, 2, 8, 6 };
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

    @Test(expected = NoSuchElementException.class) public void _07_옵션_타입() {
        Optional<String> optional = Optional.empty();
        optional.get();
    }

    @Test public void _07_옵션_다루기() {
        final Optional<String> optional = words.stream().filter(w -> w.contains("red")).findFirst();
        try {
            optional.ifPresent(v -> {
                throw new RuntimeException();
            });
            assert false; // 이 행은 실행되면 안됨.
        } catch (RuntimeException e) {

        }

        // 비어있는 경우는 실행되지 않음.
        Optional.empty().ifPresent(v->{throw new RuntimeException();});

        Set<String> results = new HashSet<>();
        optional.ifPresent(results::add);
        assertThat(results.contains("tired"), is(true));

        // 실행 결과를 받고 싶은 경우에는 map 사용.
        results = new HashSet<>();
        Optional<Boolean> added = optional.map(results::add);
        assertThat(added, is(Optional.of(Boolean.TRUE)));

        // 대상이 빈경우에는 empty Optional 반환
        Optional<Boolean> a = Optional.empty().map(v -> true);
        assertThat(a.isPresent(), is(false));

        Optional<String> emptyOptional = Optional.empty();

        // orElse로 기본값 지정 가능
        String result = emptyOptional.orElse("기본값");
        assertThat(result, is("기본값"));

        // 기본값 생성하는 코드 호출 가능
        result = emptyOptional.orElseGet(() -> System.getProperty("user.dir"));
        assertThat(result, is(System.getProperty("user.dir")));

        // 값이 없는 경우 예외 던지기
        try {
            emptyOptional.orElseThrow(NoSuchElementException::new);
            assert false;
        } catch (NoSuchElementException e) {

        }
    }

    public static Optional<Double> inverse(Double x) {
        return x == 0 ? Optional.empty() : Optional.of(1 / x);
    }

    @Test public void _07_옵션값_생성() {
        assertThat(inverse(0d), is(Optional.empty()));
        assertThat(inverse(10d), is(Optional.of(0.1d)));

        // ofNullalbe -> null 값 처리
        assertThat(Optional.ofNullable(null), is(Optional.empty()));
        assertThat(Optional.ofNullable("test"), is(Optional.of("test")));
    }

    public static Optional<Double> squareRoot(Double x) {
        return x < 0 ? Optional.empty() : Optional.of(Math.sqrt(x));
    }

    @Test public void _07_옵션값_합성() {
        Optional<Double> result = inverse(4.0d).flatMap(Ch02ExampleTest::squareRoot);
        assertThat(result, is(Optional.of(0.5d)));

        Optional<Double> result2 = Optional.of(-4.0d)
                .flatMap(Ch02ExampleTest::inverse)
                .flatMap(Ch02ExampleTest::squareRoot);
        assertThat("inverse, squareRoot중 하나라도 empty 리턴하면, 결과는 empty", result2, is(Optional.<Double>empty()));
    }

    @Test public void _08_리덕션() {
        Stream<Integer> values = Stream.of(digits);
        // x 이전값, y 지금 요소 (v0 + v1 + v2.....)
        Optional<Integer> sum = values.reduce((x, y) -> x + y);

        assertThat(sum, is(Optional.of(366)));

    }

    @Test public void _08_리덕션_병렬_수행확인() {
        words.stream().sorted().parallel().reduce("", (x, y) -> {
            // 정렬을 했기 때문에 숫서대로 리듀스 되어야하나, 병렬처리해서
            // 순서가 꼬여서 출력되어야 한다.
            System.out.println(y);
            return x + y;
        });
    }

//    컴파일러 내부 오류로 컴파일 되지 않음..(java 1.8.20 for mac) ㅡ.ㅡ?
//    @Test public void _08_리덕션2() {
//        // 초기값, 누적함수, 결합함수(병렬처리시..)
//        int sum = words.stream().reduce(0, (total, word) -> total + word.length(), (t1, t2) -> t1 + t2);
//        assertThat(sum, is(9999));
//    }

    @Test public void _09_결과모으기() {
        // collect로 모으기
        HashSet<String> result = words.stream().collect(HashSet::new, HashSet::add, HashSet::addAll);

        // 통계 뽑기(합계, 평균, 최댓값, 최솟값등..)
        IntSummaryStatistics summary = words.stream().collect(Collectors.summarizingInt(String::length));
        assertThat(summary.getCount(), is(9989L));
        assertThat(summary.getAverage(), is(3.9401341475623184D));
        assertThat(summary.getMax(), is(14));
    }

    @Test public void _10_toMap() {
        Stream<Locale> localeStream = Stream.of(Locale.getAvailableLocales());
        Map<String, String> languageNames = localeStream.collect(Collectors.toMap(
                l -> l.getDisplayLanguage(), // key 맵핑
                l -> l.getDisplayLanguage(l), // value 맵핑
                (existingValue, newValue) -> existingValue)); // key가 충돌하는 경우 머지 함수
        assertThat(languageNames.get("우크라이나어"), is("українська"));
        assertThat(languageNames.get("태국어"), is("ไทย"));
    }

    @Test public void _11_그룹핑() {
        final Stream<Locale> stream = Stream.of(Locale.getAvailableLocales());
        Map<String, List<Locale>> countryToLocales = stream.collect(
                Collectors.groupingBy(Locale::getCountry) // 묶을 기준값
        );
        System.out.println(countryToLocales.get("CH"));
    }


}