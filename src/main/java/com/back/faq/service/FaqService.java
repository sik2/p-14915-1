package com.back.faq.service;

import com.back.faq.entity.Faq;
import com.back.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {
    private final FaqRepository faqRepository;

    // 컨트롤러가 쓰는 검색 메서드. Flux를 그대로 돌려준다. DB 결과를 기다리며 멈추지 않고, 결과가 도착하면 그때 응답에 쓰인다.
    public Flux<Faq> search(String keyword) {
        return faqRepository.searchByKeyword(keyword);
    }

    // AI가 호출할 수 있는 도구. 반환값은 JSON으로 바뀌어 AI에게 전달된다.
    // 리포지터리는 Flux를 돌려주지만, 도구는 실제 데이터(List)를 반환해야 한다. (10강에서 자세히 다룬다)
    @Tool(description = "FAQ 데이터베이스에서 키워드로 질문과 답변을 검색합니다. 회원가입, 배송, 환불, 교환, 포인트, 쿠폰 등의 질문에 사용하세요.")
    public List<Faq> searchFaq(
            @ToolParam(description = "검색할 키워드 (예: 배송, 환불, 회원가입)") String keyword
    ) {
        return search(keyword)
                .collectList()
                .block();
    }
}
