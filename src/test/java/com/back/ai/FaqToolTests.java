package com.back.ai;

import com.back.TestcontainersConfiguration;
import com.back.ai.service.ChatService;
import com.back.faq.service.FaqService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class FaqToolTests {
    @Autowired
    private FaqService faqService;
    @Autowired
    private ChatService chatService;

    @Test
    @DisplayName("@Tool 메서드가 도구로 등록되고, 실행 결과가 FAQ JSON으로 AI에게 전달된다")
    void t1() {
        ToolCallback[] callbacks = ToolCallbacks.from(faqService); // .tools(faqService)가 내부에서 하는 일
        assertEquals(1, callbacks.length);
        assertEquals("searchFaq", callbacks[0].getToolDefinition().name());

        // AI가 {"keyword":"배송"}으로 도구를 호출했을 때 AI가 받게 되는 문자열
        String toolResult = callbacks[0].call("{\"keyword\":\"배송\"}");
        System.out.println("도구 실행 결과: " + toolResult);
        assertTrue(toolResult.contains("배송은 얼마나 걸리나요?"));
    }

    @Test
    @DisplayName("FAQ 기반 AI 챗봇 - 한 번에 답변 받기")
    void t2() {
        String response = chatService.chat("배송은 얼마나 걸려요?");

        assertNotNull(response);
        assertFalse(response.isEmpty());
        System.out.println("AI 응답: " + response);
    }
}
