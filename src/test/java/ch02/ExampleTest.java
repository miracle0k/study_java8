package ch02;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ExampleTest {
    private List<String> words;

    @Before public void setUp() throws Exception{
        String contents = new String(Files.readAllBytes(Paths.get("./ch02/alice.txt")), StandardCharsets.UTF_8);
        words = Arrays.asList(contents.split("[\\P{L}]+"));
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