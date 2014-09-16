package ch01;

/**
 * Created by naver on 2014. 9. 16..
 */
public interface Person {
    long getId();
    // 디폴트 메소드
       default String getName() {
        return "John Q. Public";
    }
}
