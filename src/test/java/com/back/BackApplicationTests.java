package com.back;

import com.back.faq.repository.FaqRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
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
}
