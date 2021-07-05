package com.sparta.springcore.model;

import com.sparta.springcore.dto.ProductRequestDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Nested
    @DisplayName("회원이 요청한 관심상품 객체 생성")
    class CreateUserProduct {
        // CreateUserProduct의 멤버변수 선언. 아래에서 계속 쓸 수 있게 됨!
        private Long userId;
        private String title;
        private String image;
        private String link;
        private int lprice;

        @BeforeEach // 한번 설정해주면 이후 등장하는 요소들의 기본값이 아래와 같음. 매번 정해줄 필요 없고, 수정 필요한 부분만 해당 메소드 내에서 수정해서 쓰면 됨.
        void setup() {
            userId = 100L;
            title = "오리온 꼬북칩 초코츄러스맛 160g";
            image = "https://shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";
            link = "https://search.shopping.naver.com/gate.nhn?id=24161228524";
            lprice = 2350;
        }

        @Test // test를 수행할 function에 붙여야 함.
        @DisplayName("정상 케이스")  // test 수행할 케이스에 이름 붙이는 것. '정상 케이스'라는 이름으로 테스트를 하는 것. 상품을 등록하는 기능을 검사할 것임.
        void createProduct_Normal() {
            // given (임의 지정)
            ProductRequestDto requestDto = new ProductRequestDto(//위에서 지정한 값을 토대로 requestDto를 생성.
                    // 뭘 보냐면 requestDto와 내가 위에서 지정한 값이 requestDto에도 요소 하나하나 잘 입력되는지 봐야함.
                    title,
                    image,
                    link,
                    lprice
            );

            // when
            Product product = new Product(requestDto, userId);  // 내가 위에서 만든 requestDto, userId를 잘 가져와서 new Product를 만들어본다.

            // then
            assertNull(product.getId()); // 기본 id값이 null인지 확인. 일치하면 통과.
            assertEquals(userId, product.getUserId()); // 내가 입력한 userId(100L)와 내가 생성한 new Product의 userId가 일치하는지
            assertEquals(title, product.getTitle()); // 이하 동
            assertEquals(image, product.getImage());
            assertEquals(link, product.getLink());
            assertEquals(lprice, product.getLprice());   //만약 기대값 : lprice, 실제값 : product.getMyprice()로 해놓으면 ~~ Expected:2350, Actual:0 띄우며 오류 발생.
            assertEquals(0, product.getMyprice()); // 내가 Product 클래스에서 myprice를 0으로 해놓았으니, 정말 0이 산출되는지 확인.
        }

        @Nested // 상위클래스 내부의 케이스라는 것을 명시
        @DisplayName("실패 케이스")
        class FailCases {
            @Nested
            @DisplayName("회원 Id")
            class userId {
                @Test
                @DisplayName("null") // userId가 null일 경우
                void fail1() {
                    // given
                    userId = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> { // 아래 new Product(requestDtom userId) 실행할 때 메시지 throw 해줘라.
                        new Product(requestDto, userId);
                    });

                    // then             //이 expected 부분은 어디서 가져온걸까 ==> Product.java => if (userId = =null || userId < 0) 부분임.
                                        //
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage()); // 예상 메시지와 실제 메시지 비교
                }

                @Test
                @DisplayName("마이너스")
                void fail2() {
                    // given
                    userId = -100L;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            // 아래 내용들(제목 url이 뭔지, 사진은 뭔지 등 BeforeEach에 의해 자동으로 채워짐
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("회원 Id 가 유효하지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품명")
            class Title {
                @Test
                @DisplayName("null")
                void fail1() {
                    // given
                    title = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("빈 문자열")
                void fail2() {
                    // given
                    String title = "";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("저장할 수 있는 상품명이 없습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 이미지 URL")
            class Image {
                @Test
                @DisplayName("null")
                void fail1() {
                    // given
                    image = null;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
                    // given
                    image = "shopping-phinf.pstatic.net/main_2416122/24161228524.20200915151118.jpg";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 이미지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가 페이지 URL")
            class Link {
                @Test
                @DisplayName("null")
                void fail1() {
                    // given
                    link = "https";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }

                @Test
                @DisplayName("URL 포맷 형태가 맞지 않음")
                void fail2() {
                    // given
                    link = "https";

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가 페이지 URL 포맷이 맞지 않습니다.", exception.getMessage());
                }
            }

            @Nested
            @DisplayName("상품 최저가")
            class LowPrice {
                @Test
                @DisplayName("0")
                void fail1() {
                    // given
                    lprice = 0;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }

                @Test
                @DisplayName("음수")
                void fail2() {
                    // given
                    lprice = -1500;

                    ProductRequestDto requestDto = new ProductRequestDto(
                            title,
                            image,
                            link,
                            lprice
                    );

                    // when
                    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                        new Product(requestDto, userId);
                    });

                    // then
                    assertEquals("상품 최저가가 0 이하입니다.", exception.getMessage());
                }
            }
        }
    }
}