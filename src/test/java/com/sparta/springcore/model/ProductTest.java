package com.sparta.springcore.model;

import com.sparta.springcore.dto.ProductRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test // test를 수행할 function에 붙여야 함.
    @DisplayName("정상 케이스") // test 수행할 케이스에 이름 붙이는 것. '정상 케이스'라는 이름으로 테스트를 하는 것. 상품을 등록하는 기능을 검사할 것임.
    void createProduct_Normal() {
        // given (임의 지정)
        Long userId = 100L;
        String title = "오리온 꼬북칩 초코츄러스맛 160g";
        String image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
        String link = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
        int lprice = 2350;

        ProductRequestDto requestDto = new ProductRequestDto( //위에서 지정한 값을 토대로 requestDto를 생성.
                // 뭘 보냐면 requestDto와 내가 위에서 지정한 값이 requestDto에도 요소 하나하나 잘 입력되는지 봐야함.
                title,
                image,
                link,
                lprice
        );

        // when
        Product product = new Product(requestDto, userId); // 내가 위에서 만든 requestDto, userId를 잘 가져와서 new Product를 만들어본다.

        // then
        assertNull(product.getId()); // 기본 id값이 null인지 확인. 일치하면 통과.
        assertEquals(userId, product.getUserId()); // 내가 입력한 userId(100L)와 내가 생성한 new Product의 userId가 일치하는지
        assertEquals(title, product.getTitle()); // 이하 동
        assertEquals(image, product.getImage());
        assertEquals(link, product.getLink());
        assertEquals(lprice, product.getLprice());
        //만약 기대값 : lprice, 실제값 : product.getMyprice()로 해놓으면 ~~ Expected:2350, Actual:0 띄우며 오류 발생.
        assertEquals(0, product.getMyprice()); // 내가 Product 클래스에서 myprice를 0으로 해놓았으니, 정말 0이 산출되는지 확인.
    }
}