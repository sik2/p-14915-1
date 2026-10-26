package com.back.faq.repository;

import com.back.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    // &@~ : PGroonga 쿼리 검색 연산자. "배송 OR 환불", "배송 -조회" 같은 검색 문법을 지원한다.
    @Query(value = """
            SELECT * FROM faq
            WHERE question &@~ :keyword OR answer &@~ :keyword
            """, nativeQuery = true)
    List<Faq> searchByKeyword(@Param("keyword") String keyword);
}
