package com.back.faq;

import com.back.TestcontainersConfiguration;
import com.back.faq.entity.Faq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

// RANDOM_PORT : 실제 Netty 서버를 띄워서 요청한다. 로그에서 요청 처리 스레드 이름을 확인할 수 있다.
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class FaqControllerTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("GET /api/v1/faqs?keyword=배송 - FAQ 검색 API")
    void t1() {
        webTestClient.get()
                .uri(uri -> uri.path("/api/v1/faqs").queryParam("keyword", "배송").build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Faq.class)
                .hasSize(3);
    }
}
