package com.back.faq.repository;

import com.back.faq.entity.Faq;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

public interface FaqRepository extends R2dbcRepository<Faq, Long> {
    // R2DBC(Spring)는 &로 시작하는 단어를 이름 있는 파라미터(&name)로 해석하므로 &@~ 연산자를 쓸 수 없다.
    // &@~ 와 같은 동작을 하는 함수 pgroonga_query_text()를 사용한다.
    @Query("""
            SELECT * FROM faq
            WHERE pgroonga_query_text(question, :keyword)
               OR pgroonga_query_text(answer, :keyword)
            """)
    Flux<Faq> searchByKeyword(@Param("keyword") String keyword);
}
