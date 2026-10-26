package com.back.faq.service;

import com.back.faq.entity.Faq;
import com.back.faq.repository.FaqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    // - Flux를 그대로 반환하면 Spring AI가 데이터를 꺼내지 않고 Flux 객체 자체를 JSON으로 바꾼다. AI는 데이터를 받지 못한다.
    // - 그래서 결과를 List로 모아 반환한다. block()은 블로킹해도 되는 스레드(boundedElastic 등)에서만 부를 수 있다.
    @Tool(description = "FAQ 데이터베이스에서 키워드로 질문과 답변을 검색합니다. 회원가입, 배송, 환불, 교환, 포인트, 쿠폰 등의 질문에 사용하세요.")
    public List<Faq> searchFaq(
            @ToolParam(description = "검색할 키워드 (예: 배송, 환불, 회원가입)") String keyword
    ) {
        return search(keyword)
                .collectList()
                .block(Duration.ofSeconds(10)); // DB가 응답하지 않을 때 무한정 기다리지 않게 한다
    }
}
