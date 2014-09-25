/*
 * @(#)Exercises02.class  2014. 9. 25..
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ch02;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by naver on 2014. 9. 25..
 */
public class Exercises02 {
    public Optional<String> run() throws IOException {
        ClassLoader loader = Exercises02.class.getClassLoader();
        String classPathRoot = loader.getResource(".").getPath();
        String contents = new String(Files.readAllBytes(Paths.get(classPathRoot, "ch02/alice.txt")), StandardCharsets.UTF_8);
        List<String> words = Arrays.asList(contents.split("[\\P{L}]+"));
        System.out.println("Start filtering...");
        return words.stream().peek(w -> System.out.println("찾는중:" + w))
                .filter(w -> w.length() > 5)
                .peek(w -> System.out.println("찾음: " + w))
                .findFirst();
    }
}
