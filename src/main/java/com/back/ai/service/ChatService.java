package com.back.ai.service;

import com.back.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatClient chatClient;
    private final FaqService faqService;

    private static final String SYSTEM_PROMPT = """
            당신은 친절한 고객 서비스 챗봇입니다.
            사용자의 질문에 대해 FAQ 데이터베이스를 검색하여 정확한 답변을 제공하세요.
            검색 결과가 없으면 "해당 내용에 대한 FAQ가 없습니다. 고객센터(1234-5678)로 문의해주세요."라고 안내하세요.
            """;

    public String chat(String userMessage) {
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(UserMessage.builder().text(userMessage).build())
                .tools(faqService) // FaqService의 @Tool 메서드를 AI가 호출할 수 있게 등록
                .call()
                .content();
    }
}
