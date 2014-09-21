package ch02;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExampleTest {
    private List<String> words;

    @Before public void setUp() {
        words = new ArrayList<String>();
        words.add("ABC");
        words.add("ZED");
    }

    @Test public void 단순리덕션() {
        Optional<String> largest = words.stream().max(String::compareToIgnoreCase);
        if (largest.isPresent()) {
            System.out.println("큰값: " + largest.get());
        } else {
            System.out.println("Empty~");
        }
    }
}