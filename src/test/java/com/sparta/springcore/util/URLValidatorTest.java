package com.sparta.springcore.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLValidatorTest {

    @Test
    @DisplayName("URL 형태: 정상")
    void urlValidator1() {
        // given / 임의의 url을 입력하고,
        String url = "https://shopping-phinf.pstatic.net/main_8232398/82323985017.4.jpg";
        // when / URLValidator로 검증하고, true or false를 반환.
        boolean isValid = URLValidator.urlValidator(url);
        // then / true이면 통과
        assertTrue(isValid);
    }

    @Test
    @DisplayName("URL 형태: 비정상 (null 인 경우)")
    void urlValidator2() {
        // given
        String url = null;
        // when / URLValidator는 null을 false로 반환하게끔 되어있음. 따라서 false 반환
        boolean isValid = URLValidator.urlValidator(url);
        // then / false 반환하여 통과. asserFalse니까!
        assertFalse(isValid);
    }

    @Test
    @DisplayName("URL 형태: 비정상 (빈 문자열)")
    void urlValidator3() {
        // given
        String url = "";
        // when
        boolean isValid = URLValidator.urlValidator(url);
        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("URL 형태: 비정상 (일반 문자열)")
    void urlValidator4() {
        // given
        String url = "단위 테스트";
        // when
        boolean isValid = URLValidator.urlValidator(url);
        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("URL 형태: 비정상 (`://` 빠짐)")
    void urlValidator5() {
        // given
        String url = "httpfacebook.com";
        // when
        boolean isValid = URLValidator.urlValidator(url);
        // then
        assertFalse(isValid);
    }
}