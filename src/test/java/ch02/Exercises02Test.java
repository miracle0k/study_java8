package ch02;

import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class Exercises02Test {
    @Test public void run() throws IOException {
        Exercises02 target = new Exercises02();
        final Optional<String> actual = target.run();
        assertThat(actual, is(Optional.of("beginning")));
    }
}