package com.back.faq.controller;

import com.back.faq.entity.Faq;
import com.back.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faqs")
@RequiredArgsConstructor
public class FaqController {
    private final FaqService faqService;

    // WebFlux의 요청 처리 스레드(reactor-http-nio-N)에서 JPA(JDBC)가 실행된다.
    // DB 응답을 기다리는 동안 이 스레드는 다른 요청을 처리하지 못한다(블로킹).
    @GetMapping
    public List<Faq> search(@RequestParam String keyword) {
        System.out.println("[" + Thread.currentThread().getName() + "] FAQ 검색 (JPA) : " + keyword);
        return faqService.search(keyword);
    }
}
