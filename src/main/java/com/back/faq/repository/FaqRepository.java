package com.back.faq.repository;

import com.back.faq.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {
    @Query(value = """
            SELECT * FROM faq
            WHERE question &@~ :keyword OR answer &@~ :keyword
            """, nativeQuery = true)
    List<Faq> searchByKeyword(@Param("keyword") String keyword);
}
