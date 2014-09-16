package ch01;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * Created by naver on 2014. 9. 16..
 */
public interface Path {
    // 인터페이스의 정적 메서드
    public static java.nio.file.Path get(String first, String... more) {
        return FileSystems.getDefault().getPath(first, more);
    }
}
