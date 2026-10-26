package com.back.faq.service;

import com.back.faq.entity.Faq;
import com.back.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    // 컨트롤러와 도구가 함께 쓰는 검색 메서드. JPA라 DB 응답이 올 때까지 기다린다(블로킹).
    public List<Faq> search(String keyword) {
        return faqRepository.searchByKeyword(keyword);
    }

    // AI가 호출할 수 있는 도구. 반환값은 JSON으로 바뀌어 AI에게 전달된다.
    @Tool(description = "FAQ 데이터베이스에서 키워드로 질문과 답변을 검색합니다. 회원가입, 배송, 환불, 교환, 포인트, 쿠폰 등의 질문에 사용하세요.")
    public List<Faq> searchFaq(
            @ToolParam(description = "검색할 키워드 (예: 배송, 환불, 회원가입)") String keyword
    ) {
        return search(keyword);
    }
}
