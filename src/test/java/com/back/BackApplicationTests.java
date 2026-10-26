package com.back;

import com.back.faq.entity.Faq;
import com.back.faq.repository.FaqRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test") // 테스트는 Ollama 사용, OpenAI 자동 설정 제외
class BackApplicationTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FaqRepository faqRepository;

    @Test
    @DisplayName("애플리케이션 시작 시 pgroonga 확장이 설치되고 FAQ 12건이 생성된다")
    void t1() {
        Integer installed = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pg_extension WHERE extname = 'pgroonga'", Integer.class);
        assertEquals(1, installed);
        assertEquals(12, faqRepository.count());
    }

    @Test
    @DisplayName("PGroonga 전문 검색 - '배송'이 들어간 FAQ만 찾는다")
    void t2() {
        List<Faq> results = faqRepository.searchByKeyword("배송");

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(f ->
                f.getQuestion().contains("배송") || f.getAnswer().contains("배송")));
        System.out.println("FTS 검색 결과:");
        results.forEach(System.out::println);
    }

    @Test
    @DisplayName("PGroonga 쿼리 문법 - 'OR'로 여러 키워드를 한 번에 검색한다")
    void t3() {
        List<Faq> results = faqRepository.searchByKeyword("배송 OR 환불");

        assertEquals(5, results.size());
        assertTrue(results.stream().anyMatch(f -> f.getQuestion().contains("환불")));
    }

    @Test
    @DisplayName("PGroonga 인덱스가 생성되어 있다")
    void t4() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pg_indexes WHERE indexname = 'pgroonga_faq_index'", Integer.class);
        assertEquals(1, count);
    }
}
